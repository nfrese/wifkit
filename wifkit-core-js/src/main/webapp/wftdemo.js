
var DOMHelper = PClass.create({

	init : function(host) 
	{
		this.host = host;
	},

	nodeId : function () { 
		return "_" + this.host.wiid; 
	},
	
	node : function () { 
		return $('#' + this.nodeId()); 
	},	
	
//	addChild : function (ix, child) {
//		if (ix > 0)
//		{
//			this.node().eq(ix).after(child);
//		}
//		else
//		{
//			this.node().prepend(child);
//		}
//	},
//	
//	removeChild : function(ix) {
//		return this.node().eq(ix).detach();
//	},
	
	moveChild : function(ix, toix)
	{
		var child = this.node().children().eq(ix);
		wft_log("@ix" + child.html());
		
		if (toix >= ix)
		{
			var afterChild = this.node().children().eq(toix)
			wft_log("@toix" + afterChild.html());
			afterChild.after(child);
		}
		else
		{
			var beforeChild = this.node().children().eq(toix)
			wft_log("@toix" + beforeChild.html());
			beforeChild.before(child);
		}
		
		//var child = this.removeChild(ix);
		//this.addChild(toix, child);
	}
	
});

var WFTDemoRootPanel = WFTRoot.extend({

	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
		this.dom.nodeId = function() {
			return "insert_wft_here"
		};
	},
	
	winit : function ()
	{
	},
	
	beforeChildInit : function(k, childInstance) {
		var html = childInstance.createHtml(); 
		this.dom.node().append(html);
	},
	
	detachChild : function(childInstance) {
		var node = childInstance.dom.node();
		node.remove();
	} 

	
});

var WFTDemoListPanel = WFTList.extend({
	
	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
		this.moved.on(function(pa) {
			this.dom.moveChild(pa.ix, pa.toix);
		}, this);
	},
	
	winit : function ()
	{
	},
	
	createHtml: function() {
		return '<ul id="'+ this.dom.nodeId() +'"></ul>';
	},
	
	itemId : function (childInstance)
	{
		return 'li_' + childInstance.wiid;
	},
	
	beforeChildInit : function(ix, childInstance) {
		var html = childInstance.createHtml(); 
		var outerHtml = '<li id="'+ this.itemId(childInstance) +'">' + html + '</li>'
		this.dom.node().append(outerHtml); // TODO index!
	}, 
	
	detachChild : function(childInstance) {
		var itemNode = $('#' + this.itemId(childInstance));
		itemNode.remove();
	} 
	
});

var WFTDemoLabel = WFTItem.extend({
	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
	},
	
	createHtml: function() {
		return '<span id="'+ this.dom.nodeId() +'"></span>';
	},
	
	winit : function ()
	{
		this.properties.changed('text').on1(function() {
			this.dom.node().text(this.getProperty("text"));
		}, this);
	},	
	
});

var WFTDemoNotification = WFTItem.extend({
	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
	},
	
	createHtml: function() {
		return '<div id="'+ this.dom.nodeId() +'"/>';
	},
	
	winit : function ()
	{
		this.properties.changed('message').on1(function() {
			var div = this.dom.node().css({position: 'absolute', left: '30px', bottom: '30px', 
					border: '3px solid #ff0000', padding: "20px", 'z-index' : "10"})
				.text(this.getProperty("message")).appendTo($('body'));
			div.fadeOut(3000);
		}, this);
	},	
	
});


var WFTDemoTextField = WFTItem.extend({
	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
	},
	winit : function ()
	{
		//this.dom.node().val(this.getProperty("text")); // initial
		
		this.properties.changed('text').on1(function(pa) {
			this.dom.node().val(this.getProperty("text"));
		}, this);
		
	},
	
	createHtml: function() {
		return '<input type="text" id="'+this.dom.nodeId() +'"></input>';
	},
	
	subscribe : function(ename, pa, callback)
	{
		if (ename == 'set:text')
		{
			var _this = this;
			this.dom.node().on('input',function() {
				var currentText = _this.dom.node().val();
				callback({'k' : 'text', 'v' : currentText});
			});
		}
	},
});


var WFTDemoButton = WFTItem.extend({
	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
	},
	winit : function ()
	{
		this.properties.changed('text').on1(function() {
			this.dom.node().text(this.getProperty('text'));
		}, this);
	},	
	
	createHtml: function() {
		return '<button type="button" id="'+this.dom.nodeId()+'"></button>';
	},	
	
	subscribe : function(ename, pa, callback)
	{
		if (ename == 'clicked')
		{
			var _this = this;
			this.dom.node().on('click',function() {
					callback();
				;});
		}
	},
});

var WFTDemoSelect = WFTList.extend({
	
	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
	},
	
	winit : function ()
	{
	},
	
	createHtml: function() {
		return '<select id="'+ this.dom.nodeId() +'"></select>';
	},
	
	beforeChildInit : function(ix, childInstance) {
		var html = childInstance.createHtml(); 
		this.dom.node().append(html);
	}, 
	
	subscribe : function(ename, pa, callback)
	{
		if (ename == 'change')
		{
			var _this = this;
			this.dom.node().on('change',function() {
				var cn = _this.dom.node();
				callback({'k' : 'selectedIndex', 'v' : null});
			});
		}
	},
	
	detachChild : function(childInstance) {
		var itemNode = $('#' + childInstance.wiid);
		itemNode.remove();
	} 
	
});

var WFTDemoOption = WFTItem.extend({
	init : function ()
	{
		this._super();
		this.dom = new DOMHelper(this);
		this.clientState = new WFTProperties(this);
	},
	winit : function ()
	{
		this.properties.changed('text').on1(function(pa) {
			this.dom.node().text(this.getProperty('text'));
		}, this);
		
		this.properties.changed('selected').on1(function(pa) {
			var selected = this.getProperty('selected');
			this.dom.node().prop('selected', selected);
			this.clientState.put('selected', selected);
		}, this);
	},
	
	createHtml: function() {
		return '<option id="'+this.dom.nodeId() +'"></option>';
	},
	
	subscribe : function(ename, pa, callback)
	{
		if (ename == 'set:selected')
		{
			this.ctxt.beforeSend.on(this.updateClientState, this);
			
			var _this = this;
			this.clientState.changed('selected').on(function(pa) {
					callback({'k' : 'selected', 'v' : pa['v']});
			});
		}
	},
	
	updateClientState : function() {
		var currentSelected = this.dom.node().prop('selected');// === 'selected';
		this.clientState.put('selected', currentSelected);
	},
	
	unReg : function() {
		this.ctxt.beforeSend.un(this.updateClientState);
	}
});

///////////// register widgets

wft_registerWidgetType("RootPanel", function(cfg) {
	return new WFTDemoRootPanel();
});

wft_registerWidgetType("ListPanel", function(cfg) {
	return new WFTDemoListPanel();
});

wft_registerWidgetType("Label", function(cfg) {
	return new WFTDemoLabel();
});

wft_registerWidgetType("Notification", function(cfg) {
	return new WFTDemoNotification();
});

wft_registerWidgetType("TextField", function(cfg) {
	return new WFTDemoTextField();
});

wft_registerWidgetType("Button", function(cfg) {
	return new WFTDemoButton();
});

wft_registerWidgetType("Select", function(cfg) {
	return new WFTDemoSelect();
});

wft_registerWidgetType("Option", function(cfg) {
	return new WFTDemoOption();
});
