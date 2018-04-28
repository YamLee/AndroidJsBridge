;(function() {
	if (window.WebViewJavascriptBridge) { return }
	var messagingIframe
	var sendMessageQueue = []
	var receiveMessageQueue = []
	var messageHandlers = {}
	
	var CUSTOM_PROTOCOL_SCHEME = 'wvjbscheme'
	var QUEUE_HAS_MESSAGE = '__WVJB_QUEUE_MESSAGE__'
	var CALL_HANDLE_NAME = 'JsBridgeCall'
	var CALL_NATIVE_HANDLE_NAME = 'JsCallNative'
	
	var responseCallbacks = {}
	var uniqueId = 1
	var callHandlerName

	var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    //将Ansi编码的字符串进行Base64编码
    function encode64(input) {
    	var output = "";
    	var chr1, chr2, chr3 = "";
    	var enc1, enc2, enc3, enc4 = "";
    	var i = 0;
    	do {
    		chr1 = input.charCodeAt(i++);
    		chr2 = input.charCodeAt(i++);
    		chr3 = input.charCodeAt(i++);
    		enc1 = chr1 >> 2;
    		enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
    		enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
    		enc4 = chr3 & 63;
    		if (isNaN(chr2)) {
    			enc3 = enc4 = 64;
    		} else if (isNaN(chr3)) {
    			enc4 = 64;
    		}
    		output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
    				+ keyStr.charAt(enc3) + keyStr.charAt(enc4);
    		chr1 = chr2 = chr3 = "";
    		enc1 = enc2 = enc3 = enc4 = "";
    	} while (i < input.length);
    	return output;
    }
    //将Base64编码字符串转换成Ansi编码的字符串
    function decode64(input) {
    	var output = "";
    	var chr1, chr2, chr3 = "";
    	var enc1, enc2, enc3, enc4 = "";
    	var i = 0;

    	if (input.length % 4 != 0) {
    		return "";
    	}
    	var base64test = /[^A-Za-z0-9\+\/\=]/g;
    	if (base64test.exec(input)) {
    		return "";
    	}
    	do {
    		enc1 = keyStr.indexOf(input.charAt(i++));
    		enc2 = keyStr.indexOf(input.charAt(i++));
    		enc3 = keyStr.indexOf(input.charAt(i++));
    		enc4 = keyStr.indexOf(input.charAt(i++));
    		chr1 = (enc1 << 2) | (enc2 >> 4);
    		chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
    		chr3 = ((enc3 & 3) << 6) | enc4;

    		output = output + String.fromCharCode(chr1);
    		if (enc3 != 64) {
    			output += String.fromCharCode(chr2);
    		}
    		if (enc4 != 64) {
    			output += String.fromCharCode(chr3);
    		}
    		chr1 = chr2 = chr3 = "";
    		enc1 = enc2 = enc3 = enc4 = "";
    	} while (i < input.length);
    	return output;
    }

    function utf16to8(str) {
      var out, i, len, c;

      out = "";
      len = str.length;
      for(i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
          out += str.charAt(i);
        } else if (c > 0x07FF) {
          out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
          out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
          out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        } else {
          out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
          out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        }
      }
      return out;
    }

    function utf8to16(str) {
      var out, i, len, c;
      var char2, char3;

      out = "";
      len = str.length;
      i = 0;
      while(i < len) {
        c = str.charCodeAt(i++);
        switch(c >> 4) {
          case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
            // 0xxxxxxx
            out += str.charAt(i-1);
            break;
          case 12: case 13:
            // 110x xxxx   10xx xxxx
            char2 = str.charCodeAt(i++);
            out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
            break;
          case 14:
            // 1110 xxxx  10xx xxxx  10xx xxxx
            char2 = str.charCodeAt(i++);
            char3 = str.charCodeAt(i++);
            out += String.fromCharCode(((c & 0x0F) << 12) |
            ((char2 & 0x3F) << 6) |
            ((char3 & 0x3F) << 0));
            break;
        }
      }
      return out;
    }

	function _createQueueReadyIframe(doc) {
		messagingIframe = doc.createElement('iframe')
		messagingIframe.style.display = 'none'
		doc.documentElement.appendChild(messagingIframe)
	}

	function init(messageHandler) {
		if (WebViewJavascriptBridge._messageHandler) { throw new Error('WebViewJavascriptBridge.init called twice') }
		WebViewJavascriptBridge._messageHandler = messageHandler
		var receivedMessages = receiveMessageQueue
		receiveMessageQueue = null
		for (var i=0; i<receivedMessages.length; i++) {
			_dispatchMessageFromObjC(receivedMessages[i])
		}
	}

	function send(data, responseCallback) {
		_doSend({ data:data }, responseCallback)
	}
	
	function registerHandler(handlerName, handler) {
		messageHandlers[handlerName] = handler
	}
	
	function callHandler(handlerName, data, responseCallback) {
	    callHandlerName = handlerName;
		_doSend({ handlerName:callHandlerName, data:data }, responseCallback)
	}

	function call(func, params, responseCallback) {
	    callHandlerName = CALL_HANDLE_NAME;
		_doSend({ handlerName:callHandlerName, data:{func:func,params:params}}, responseCallback)
	}

	function _doSend(message, responseCallback) {
		if (responseCallback) {
		    console.log('send msg:'+JSON.stringify(message))
			var callbackId = 'cb_'+(uniqueId++)+'_'+new Date().getTime()
			responseCallbacks[callbackId] = responseCallback
			message['callbackId'] = callbackId
		}
		sendMessageQueue.push(message)
		messagingIframe.src = CUSTOM_PROTOCOL_SCHEME + '://' + QUEUE_HAS_MESSAGE
	}

	function _fetchQueue() {
		var messageQueueString = JSON.stringify(sendMessageQueue)
		sendMessageQueue = []
		var base64Result = encode64(utf16to8(messageQueueString));
		return base64Result
	}

	function _dispatchMessageFromObjC(messageJSON) {
		console.log('dispatchMessageFromObjC into')
		setTimeout(function _timeoutDispatchMessageFromObjC() {
			console.log('get msg:'+messageJSON)
			var message = JSON.parse(messageJSON)
			var messageHandler
			
			if (message.responseId) {
                console.log('responseId is '+message.responseId)
				var responseCallback = responseCallbacks[message.responseId]
				if (!responseCallback) { return; }
				if (callHandlerName == CALL_NATIVE_HANDLE_NAME){
				    responseCallback(message.responseData)
				} else if (callHandlerName == CALL_HANDLE_NAME){
				    var obj = JSON.parse(message.responseData)
				    console.log('response obj:'+obj)
				    responseCallback(obj)
				}
				delete responseCallbacks[message.responseId]
			} else {
				var responseCallback
				if (message.callbackId) {
					var callbackResponseId = message.callbackId
					responseCallback = function(responseData) {
						_doSend({ responseId:callbackResponseId, responseData:responseData })
					}
				}
				
				var handler = WebViewJavascriptBridge._messageHandler
				if (message.handlerName) {
					handler = messageHandlers[message.handlerName]
				}
				
				try {
					handler(message.data, responseCallback)
				} catch(exception) {
					if (typeof console != 'undefined') {
						console.log("WebViewJavascriptBridge: WARNING: javascript handler threw.", message, exception)
					}
				}
			}
		})
	}
	
	function _handleMessageFromObjC(messageJSON) {
		console.log('handle callback msg'+messageJSON)
		if (receiveMessageQueue) {
			console.log('message queue prepared')
			receiveMessageQueue.push(messageJSON)
		}
		console.log('dispatch message')
		_dispatchMessageFromObjC(messageJSON)
	}

	var WebViewJavascriptBridge = window.JSBridge = window.WebViewJavascriptBridge = {
		init: init,
		send: send,
		registerHandler: registerHandler,
		callHandler: callHandler,
		_fetchQueue: _fetchQueue,
		call:call,
		_handleMessageFromObjC: _handleMessageFromObjC
	}

	var doc = document
	_createQueueReadyIframe(doc)
	var readyEvent = doc.createEvent('Events')
	readyEvent.initEvent('WebViewJavascriptBridgeReady')
	readyEvent.bridge = WebViewJavascriptBridge
	doc.dispatchEvent(readyEvent)
})();
