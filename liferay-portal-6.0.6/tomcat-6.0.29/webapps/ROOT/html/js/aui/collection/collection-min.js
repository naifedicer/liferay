/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.2.0
build: nightly
*/
YUI.add("array-extras",function(d){var b=d.Lang,c=Array.prototype,a=d.Array;a.lastIndexOf=(c.lastIndexOf)?function(e,f){return e.lastIndexOf(f);}:function(e,g){for(var f=e.length-1;f>=0;f=f-1){if(e[f]===g){break;}}return f;};a.unique=function(f,h){var e=f.slice(),g=0,k=-1,j=null;while(g<e.length){j=e[g];while((k=a.lastIndexOf(e,j))!==g){e.splice(k,1);}g+=1;}if(h){if(b.isNumber(e[0])){e.sort(a.numericSort);}else{e.sort();}}return e;};a.filter=(c.filter)?function(e,g,h){return c.filter.call(e,g,h);}:function(e,h,i){var g=[];a.each(e,function(k,j,f){if(h.call(i,k,j,f)){g.push(k);}});return g;};a.reject=function(e,g,h){return a.filter(e,function(k,j,f){return !g.call(h,k,j,f);});};a.every=(c.every)?function(e,g,h){return c.every.call(e,g,h);}:function(g,j,k){for(var h=0,e=g.length;h<e;h=h+1){if(!j.call(k,g[h],h,g)){return false;}}return true;};a.map=(c.map)?function(e,g,h){return c.map.call(e,g,h);}:function(e,h,i){var g=[];a.each(e,function(k,j,f){g.push(h.call(i,k,j,f));});return g;};a.reduce=(c.reduce)?function(e,i,g,h){return c.reduce.call(e,function(l,k,j,f){return g.call(h,l,k,j,f);},i);}:function(e,j,h,i){var g=j;a.each(e,function(l,k,f){g=h.call(i,g,l,k,f);});return g;};a.find=function(g,j,k){for(var h=0,e=g.length;h<e;h++){if(j.call(k,g[h],h,g)){return g[h];}}return null;};a.grep=function(e,f){return a.filter(e,function(h,g){return f.test(h);});};a.partition=function(e,h,i){var g={matches:[],rejects:[]};a.each(e,function(j,f){var k=h.call(i,j,f,e)?g.matches:g.rejects;k.push(j);});return g;};a.zip=function(f,e){var g=[];a.each(f,function(i,h){g.push([i,e[h]]);});return g;};a.forEach=a.each;},"3.2.0");YUI.add("arraylist",function(e){var d=e.Array,c=d.each,a;function b(f){if(f!==undefined){this._items=e.Lang.isArray(f)?f:d(f);}else{this._items=this._items||[];}}a={item:function(f){return this._items[f];},each:function(g,f){c(this._items,function(j,h){j=this.item(h);g.call(f||j,j,h,this);},this);return this;},some:function(g,f){return d.some(this._items,function(j,h){j=this.item(h);return g.call(f||j,j,h,this);},this);},indexOf:function(f){return d.indexOf(this._items,f);},size:function(){return this._items.length;},isEmpty:function(){return !this.size();}};a._item=a.item;b.prototype=a;e.mix(b,{addMethod:function(f,g){g=d(g);c(g,function(h){f[h]=function(){var j=d(arguments,0,true),i=[];c(this._items,function(m,l){m=this._item(l);var k=m[h].apply(m,j);if(k!==undefined&&k!==m){i.push(k);}},this);return i.length?i:this;};});}});e.ArrayList=b;},"3.2.0");YUI.add("arraylist-add",function(a){a.mix(a.ArrayList.prototype,{add:function(d,c){var b=this._items;if(a.Lang.isNumber(c)){b.splice(c,0,d);}else{b.push(d);}return this;},remove:function(e,d,b){b=b||this.itemsAreEqual;for(var c=this._items.length-1;c>=0;--c){if(b.call(this,e,this.item(c))){this._items.splice(c,1);if(!d){break;}}}return this;},itemsAreEqual:function(d,c){return d===c;}});},"3.2.0",{requires:["arraylist"]});YUI.add("arraylist-filter",function(a){a.mix(a.ArrayList.prototype,{filter:function(c){var b=[];a.Array.each(this._items,function(e,d){e=this.item(d);if(c(e)){b.push(e);}},this);return new this.constructor(b);}});},"3.2.0",{requires:["arraylist"]});YUI.add("array-invoke",function(a){a.Array.invoke=function(b,e){var d=a.Array(arguments,2,true),f=a.Lang.isFunction,c=[];a.Array.each(a.Array(b),function(h,g){if(f(h[e])){c[g]=h[e].apply(h,d);}});return c;};},"3.2.0");YUI.add("collection",function(a){},"3.2.0",{use:["array-extras","arraylist","arraylist-add","arraylist-filter","array-invoke"]});