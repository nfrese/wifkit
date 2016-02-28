
var WFTDomHelper = PClass.create({

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

var WFTDomRoot = WFTRoot.extend({

	init : function ()
	{
		this._super();
		this.dom = new WFTDomHelper(this);
		this.dom.nodeId = function() {
			return "wft_dom_root"
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

var WFTDomElement = WFTList.extend({
	
	init : function ()
	{
		this._super();
		this.dom = new WFTDomHelper(this);
		this.moved.on(function(pa) {
			this.dom.moveChild(pa.ix, pa.toix);
		}, this);
	},
	
	winit : function ()
	{
	},
	
	createHtml: function() {
		var name = this.properties.get('name');
		var text = this.properties.get('text');
		var attributeHtml = '';
		
		var attrProps = this.properties.startingWith('attr:');

		$.each(attrProps, function(key, value) {
			var attrName = key.replace(/^attr\:/, '');
			attributeHtml += attrName + '="' + value + '" ';
		});
		
		return '<' + name + ' id="'
			+ this.dom.nodeId() +'"' 
			+ attributeHtml +'>'
			+ (text != null ? text : '') 
			+ '</' + name + '>';
	},
	
	beforeChildInit : function(ix, childInstance) {
		var html = childInstance.createHtml(); 
		this.dom.node().append(html); 
	}, 
	
	detachChild : function(childInstance) {
		var itemNode = $('#' + this.itemId(childInstance));
		itemNode.remove();
	}, 
	
	subscribe : function(ename, pa, callback)
	{
		var _this = this;
		this.dom.node().on(ename, function() 
		{
			
			var propName = pa['propname'];
			var value = null;
			
			if (propName = 'attr:value')
			{
				value = _this.dom.node().val();
			}
			else if (propName = 'text')
			{
				value = _this.dom.node().text();
			}
			
			callback({'k' : propName, 'v' : value});
		});
	},
	
});


///////////// register widgets

wft_registerWidgetType("DomRoot", function(cfg) {
	return new WFTDomRoot();
});

wft_registerWidgetType("DomElement", function(cfg) {
	return new WFTDomElement();
});
