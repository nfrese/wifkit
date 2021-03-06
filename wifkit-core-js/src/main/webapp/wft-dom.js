
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
		this.clientState = new WFTProperties(this);
		
		this.moved.on(function(pa) {
			this.dom.moveChild(pa.ix, pa.toix);
		}, this);
	},
	
	winit : function ()
	{
		this.properties.anyChanged().on(function(pa) {
			var key = pa['k'];
			var value = pa['v'];
			if (wft_starts_with(key, 'attr:')) 
			{
				var attrName = key.replace(/^attr\:/, '');
				this.dom.node().attr(attrName, value);
			}
			else if (key == 'text')
			{
				this.dom.node().text(value);
			}
			else
			{
				wft_log("unexpected property change" + pa);
			}
		});
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
		var itemNode = $('#' + childInstance.dom.nodeId());
		itemNode.remove();
	}, 
	
	subscribe : function(ename, pa, callback)
	{
		var _this = this;
		
		if (ename == 'watch')
		{
			var propName = pa['propname'];
			
			this.ctxt.beforeSend.on(function() {
				var currentValue = this.dom.node().prop(propName);
				this.clientState.put(propName, currentValue);
				
				this.clientState.changed(propName).on(function(pa) {
					callback({'k' : pa['k'], 'v' : pa['v']});
				});
			}, this);
			
		}
		else
		{
			this.dom.node().on(ename, function() {

				var propName = pa['propname'];
				var value = null;

				if (propName == 'attr:value')
				{
					value = _this.dom.node().val();
				}
				else if (propName == 'text')
				{
					value = _this.dom.node().text();
				}

				callback({'k' : propName, 'v' : value});
			});
		}
	},
	
	svr_dominvoke : function (call)
	{
		var node = this.dom.node();
		node[call['method']].apply(node, call['pa']);
	}
	
});


///////////// register widgets

wft_registerWidgetType("DomRoot", function(cfg) {
	return new WFTDomRoot();
});

wft_registerWidgetType("DomElement", function(cfg) {
	return new WFTDomElement();
});
