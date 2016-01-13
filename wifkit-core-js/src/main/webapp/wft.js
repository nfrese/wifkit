//////////// js object-oriented helper ///////////
//
// origin: https://www.ruzee.com/blog/2008/12/javascript-inheritance-via-prototypes-and-closures/
//
//////////////////////////////////////////////////

(function(){
	  var isFn = function(fn) { return typeof fn == "function"; };
	  PClass = function(){};
	  PClass.create = function(proto) {
	    var k = function(magic) { // call init only if there's no magic cookie
	      if (magic != isFn && isFn(this.init)) this.init.apply(this, arguments);
	    };
	    k.prototype = new this(isFn); // use our private method as magic cookie
	    for (key in proto) (function(fn, sfn){ // create a closure
	      k.prototype[key] = !isFn(fn) || !isFn(sfn) ? fn : // add _super method
	        function() { this._super = sfn; return fn.apply(this, arguments); };
	    })(proto[key], k.prototype[key]);
	    k.prototype.constructor = k;
	    k.extend = this.extend || this.create;
	    return k;
	  };
	})();

/////////////////// wifkit ///////////////////////

function wft_error(message)
{
	alert("wft_error: " + message);
}

function wft_log(message)
{
	console.log("wft: " + message);
}

function wft_setInitialResponse(json)
{
	window.wft_initialResponse=json;	
}

function wft_registerWidgetType(tp, instanceFn)
{
	if (typeof window.wft_widgetFactories == 'undefined') {
		window.wft_widgetFactories = {};
	}
	
	window.wft_widgetFactories[tp] =  instanceFn; 
}

function wft_onReady()
{
	wft_runEvents(window.wft_initialResponse);
}

function wft_runEvents(eventsStruct)
{
	var evts = eventsStruct['events'];
	
	$.each(evts, function( index, entry ) {
		
		  var wiid = entry['wiid'];
		  var etype = entry['etype'];
		  var pa = entry['pa'];
		  
		  if (etype === 's_page')
		  {
			  window.wft_rootInstance = wft_createInstance(wiid, pa);
			  window.wft_rootInstance.winit();
		  }
		  
		  if (wiid != null)
		  {
			  var instance = window.wft_instances[wiid];
			  if (instance != null)
		      {
				  var methodName = "svr_" + etype;
				  var method = instance[methodName];
				  if (typeof method == 'undefined')
				  {
					  wft_error("method " + methodName + " not found on " + instance);
				  }
				  
				  method.call(instance, pa);
		      }
		  }
    });
}

function wft_createInstance(parent, pa)
{
	var tp = pa['tp'];
	var wiid = pa['wiid'];
	var initprops = pa['initprops'];
	var factory = window.wft_widgetFactories[tp];
	if (factory != null)
	{
		var instance = factory();
		instance.setWiid(wiid);
		if (parent != null)
		{
			instance.setCtxt(parent.ctxt);
		}
		else
		{
			// probably root
			instance.setCtxt(new SMCtxt(wiid));
		}
		
		if (typeof window.wft_instances == 'undefined') {
			window.wft_instances = {};
		}
		
		instance.properties.putAll(initprops);
		
		window.wft_instances[wiid] = instance; 		
		return instance;
	}
	else
	{
		alert("cannot find widget-factory for: " + tp);
	}
}

function wft_equals(obj1, obj2)
{
	var r = JSON.stringify(obj1)===JSON.stringify(obj2);
	return r;
}

function wft_move(arr, ix, toix)
{
	var cutElement = arr.splice(ix, 1)[0];
	arr.splice(toix, 0, cutElement);
}

$( document ).ready(function() {
	 wft_log('wft_ready');
	 wft_onReady();
});


//////////////////////////////////

// event observer

var Observer = PClass.create({

	init : function() 
	{
		this.fns = [];
	},
	
    on : function(fn, scope) {
        this.fns.push({ fn: fn, scope: scope });
    },
    
    on1 : function(fn, scope) { // register and call in one 
        this.on(fn, scope);
        fn.call(scope);
    },

    un : function(fn) {
        this.fns = this.fns.filter(
            function(el) {
                if ( el.fn !== fn ) {
                    return el;
                }
            }
        );
    },

    fire : function(o, thisObj) {
        var scope = thisObj || window;
        this.fns.forEach(
            function(el) {
                el.fn.call(el.scope || scope, o);
            }
        );
    }
});

// context (event queues)

var SMCtxt = PClass.create({

	init: function(pageWiid) { 
		this.pageWiid = pageWiid;
		this._eventQueue = [];
		this.callPending = false;
		this.beforeSend = new Observer();
	}, 
	
	enqueueEvent : function(key, event, delayMs)
	{
		// remove event with the same key
		var newEventQueue = this._eventQueue.filter(function(elem){
			   return elem.key != key ; 
		});
		
		this._eventQueue = newEventQueue; 
		
		// append event
		this._eventQueue.push({'key': key, 'event' : event});
		
		var _this = this;
		if (delayMs > -1)
		{
			setTimeout(function() { _this.sendEvents() }, delayMs);
		}
	},
	
	sendEvents : function()
	{
		if (this._eventQueue.length == 0)
		{
			return;
		}
		if (this.callPending) return;
		
		this.beforeSend.fire(null, this);
		
		var events = []; 
		$.each(this._eventQueue, 
				function( index, entry ) {
					events.push(entry.event)
				});
		
		this._svr_call(events);
		this._eventQueue = []; // empty queue
	},
	
	_svr_call : function (events)
	{
		var data = { 'pagewiid': this.pageWiid, 'events': events }; 

		var myurl= 'wft';
		
		this.callPending = true;
		
		var _this = this;
		
		$.ajax( {
			url : myurl,
			data : JSON.stringify(data),
			contentType : 'application/json',
			type : 'POST',
			dataType: 'json',
			async: true,
			success: function (responseJson,b,c) {
				_this.callPending = false;
				wft_runEvents(responseJson);
				_this.sendEvents();
			}
		})
	}
});

// classes

var WFTAtom = PClass.create({  // base-class
	
	init: function() { 
		this.ctxt = null;
	}, 
	
	setWiid: function(wiid_) 
	{
		this.wiid = wiid_;
	},
	
	setCtxt: function(ctxt)
	{
		this.ctxt = ctxt;
	},

	invoke : function(method, params, delayMs)
	{
		var key = this.wiid + '_' + method;
//		if (typeof subtype != 'undefined' && subtype != null)
//		{
//			key += ':' + subtype;
//		}
		this.ctxt.enqueueEvent(key, { 'wiid': this.wiid, 'etype' : method, 'pa' : params}, delayMs);
	}

});

var WFTProperties = PClass.create({

	init : function(thisForListeners) 
	{
		this._thisForListeners = thisForListeners;
		this._map = {};
		this._observers = {};
	},
	
	put : function(k,v)
	{
		var oldValue = this._map[k]; 
		this._map[k] = v;
		if (!wft_equals(oldValue, v))
		{
			this._fireChanged(k, {k: k, v: v});
		}
	},
	
	putAll : function(jsObj)
	{
		this._map = jsObj;
	},
	
	get : function(k)
	{
		return this._map[k];
	},
	
	_fireChanged : function(k, pa)
	{
		if (typeof this._observers[k] != 'undefined')
		{
			this._observers[k].fire(pa, this._thisForListeners);
		}
	},
	
	changed : function(k) // create observer on demand
	{
		if (typeof this._observers[k] == 'undefined')
		{
			this._observers[k] = new Observer(); 
		}
		return this._observers[k];
	}
	
});	

var WFTItem = WFTAtom.extend({

	init : function () {
		this.properties = new WFTProperties(this);
		this._super();
	},	
	
	winit : function (parent) {},
	detachMe : function (parent) {},
	subscribe : function(eventName, pa, callback) {},
	
	svr_s_putprop : function(pa)
	{
		var k = pa['k'];
		var v = pa['v'];
		this.properties.put(k, v);
	},
	
	svr_s_subscribe : function(pa)
	{
		var _this = this;
		this.subscribe(pa['ename'], pa, function(params)
		{           
			_this.invoke(pa['ename'], params, pa['delayms']);
		}
		);
		
		wft_log(pa);
	},
	
	getProperty : function (k) { return this.properties.get(k); },
	
	poll4Changes : function() {} // for widgets like option
	
});

var WFTMap = WFTItem.extend({
	
	init : function () {
		this._super();
		this._map = {};
	},

	svr_s_put : function(pa)
	{
		wft_log("svr_s_put", pa);
		var k = pa['k'];
		var v = pa['v'];
		if (v.tp != null)
		{
			var childInstance = wft_createInstance(this, v);
			this._map[k] = childInstance;
			this.beforeChildInit(k, childInstance);
			childInstance.winit(this);
		}
		else
		{
			wft_error("svr_s_put expected instance");
		}
	},
	
	svr_s_rme : function(pa)
	{
		var k = pa['k'];
		var childInstance = this._map[k];
		childInstance.detachMe(this);
		detachChild(childInstance);
		delete this._map[k];
		
	},
	
	beforeChildInit : function(k, childInstance) {},
	
	detachChild : function(childInstance) {} 
	
});

var WFTList = WFTItem.extend({
	
	init : function () {
		this._super();
		this._list = [];
		this.moved = new Observer();
	},
	
	svr_s_add : function(pa)
	{
		wft_log("svr_s_add ", pa);
		var childInstance = wft_createInstance(this, pa);
		var ix = this._list.length;
		this._list.push(childInstance);
		this.beforeChildInit(ix, childInstance);
		childInstance.winit(this);
	},
	
	svr_s_ins : function(pa)
	{
		wft_log("svr_s_ins ", pa);
		var ix = pa["ix"];
		var childInstance = wft_createInstance(this, pa["o"]);
		var len = this._list.length;
		if (ix > len - 1)
		{
			wft_error("ix > len - 1: " + ix + " len= " + len);
			return;
		}
		
		this._list.splice(pa["o"], 0, childInstance);
		this.beforeChildInit(ix, childInstance);
		childInstance.winit(this);
	},
	
	svr_s_mv : function(pa)
	{
		wft_log("svr_s_mv ", pa);
		var ix = pa["ix"];
		var toix = pa["toix"]
		var len = this._list.length;
		if (ix >= len || toix >= len)
		{
			wft_error("ix >= len | txto >= len ix=" + ix + " ixto= " + toix);
			return;
		}
		
		wft_move(this._list, ix, toix);
		this.moved.fire({ ix: ix, toix: toix });
	},

	
	svr_s_rm : function(pa)
	{
		var wiid = pa['wiid'];
		var childInstance = null;
		$.each(this._list, function( index, entry ) {
			if (entry.wiid == wiid) {
				childInstance = entry;
			}
		}
		);
		
		childInstance.detachMe(this);
		this.detachChild(childInstance);
		
		var newList = this._list.filter(function(elem){
			   return elem.wiid != wiid ; 
			});
		
		this._list = newList;
		
	},
	
	beforeChildInit : function(ix, childInstance) {}, 
	
	detachChild : function(childInstance) {}
});

var WFTRoot = WFTMap.extend({
	
	init: function()
	{
		this._super();
	},

	svr_s_alert : function(pa)
	{
		alert(pa);
	}
	
});



