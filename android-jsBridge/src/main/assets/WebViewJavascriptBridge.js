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
		return messageQueueString
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
