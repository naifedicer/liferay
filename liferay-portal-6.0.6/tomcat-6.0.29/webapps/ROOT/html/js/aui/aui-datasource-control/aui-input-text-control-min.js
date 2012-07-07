AUI.add("aui-input-text-control",function(J){var l=J.Lang,Q=l.isArray,K=l.isFunction,G=l.isString,e="alert",a="bindUI",c="content",X="icon",f="list",N="loading",W="renderUI",O="circle-triangle-b",d=e,B=N,M=8,g=9,Y=13,H=16,C=17,D=18,L=20,Z=27,F=33,I=35,S=36,h=38,T=40,R=39,P=37,b=44,k=44,E=229,U={node:null,points:["tl","bl"]},j="boundingBox",V="contentBox";var i=function(){var A=this;A.on(W,A._renderUIInputTextControl,A);A.on(a,A._bindUIInputTextControl,A);};i.ATTRS={button:{value:true},delimChar:{value:null,setter:function(A){if(G(A)&&(A.length>0)){A=[A];}else{if(!Q(A)){A=J.Attribute.INVALID_VALUE;}}return A;}},forceSelection:{value:false},iconButton:{value:O},input:{value:null},matchKey:{value:0},minQueryLength:{value:1},queryDelay:{value:0.2,getter:function(A){return A*1000;}},queryInterval:{value:0.5,getter:function(A){return A*1000;}},queryMatchCase:{value:false},queryMatchContains:{value:false},queryQuestionMark:{value:true},suppressInputUpdate:{value:false},typeAhead:{value:false},typeAheadDelay:{value:0.2,getter:function(A){return A*1000;}},uniqueName:{value:null}};i.prototype={initializer:function(m){var A=this;A._overlayAlign=J.mix({},U);},_renderUIInputTextControl:function(){var A=this;A._renderInput();},_bindUIInputTextControl:function(){var A=this;A._bindDataSource();var m=A.button;var n=A.inputNode;n.on("blur",A._onTextboxBlur,A);n.on("focus",A._onTextboxFocus,A);n.on("keydown",A._onTextboxKeyDown,A);n.on("keypress",A._onTextboxKeyPress,A);n.on("keyup",A._onTextboxKeyUp,A);A.publish("handleResponse");A.publish("textboxKeyDown");A.publish("textboxKeyPress");A.publish("textboxKeyUp");A.publish("invalidQueryLength");A.publish("sendQueryDisabled");A.publish("containerCollapse");A.publish("containerExpand");A.publish("containerPopulate");A.publish("itemArrowFrom");A.publish("itemArrowTo");A.publish("itemMouseOut");A.publish("itemMouseOver");A.publish("itemSelect");A.publish("selectionEnforce");A.publish("textboxBlur");A.publish("textboxChange");A.publish("textboxFocus");A.publish("textboxKey");A.publish("typeAhead");A.publish("unmatchedItemSelect");},syncUI:function(){var A=this;A.inputNode.setAttribute("autocomplete","off");},doBeforeLoadData:function(A){return true;},formatResult:function(m,n,A){return A||"";},generateRequest:function(A){return{request:A};},handleResponse:function(n){var m=this;m.fire("handleResponse",n);var A=m.get("iconButton")||O;if(n.error){A=d;}m.button.set(X,A);},_bindDataSource:function(){var A=this;var m=A.button;var o=A.get("dataSource");var n=A.get("dataSourceType");o.on("request",J.bind(m.set,m,X,B));o.on("error",A.handleResponse,A);o.after("response",A.handleResponse,A);},_clearInterval:function(){var A=this;if(A._queryIntervalId){clearInterval(A._queryIntervalId);A._queryIntervalId=null;}},_clearSelection:function(){var m=this;var n=m.get("delimChar");var A={previous:"",query:m.inputNode.get("value")};if(n){A=m._extractQuery(m.inputNode.get("value"));}m.fire("selectionEnforce",A.query);},_enableIntervalDetection:function(){var A=this;var m=A.get("queryInterval");if(!A._queryIntervalId&&m){A._queryInterval=setInterval(J.bind(A._onInterval,A),m);}},_extractQuery:function(p){var t=this;var r=t.get("delimChar");var A=-1;var n=r.length-1;var s,q,o;for(;n>=0;n--){s=p.lastIndexOf(r[n]);if(s>A){A=s;}}if(r[n]==" "){for(var m=r.length-1;m>=0;m--){if(p[A-1]==r[m]){A--;break;}}}if(A>-1){q=A+1;while(p.charAt(q)==" "){q+=1;}o=p.substring(0,q);p=p.substring(q);}else{o="";}return{previous:o,query:p};},_focus:function(){var A=this;setTimeout(function(){A.inputNode.focus();},1);},_isIgnoreKey:function(m){var A=this;if((m==g)||(m==Y)||(m==H)||(m==C)||(m>=D&&m<=L)||(m==Z)||(m>=F&&m<=I)||(m>=S&&m<=T)||(m>=b&&m<=k)||(m==E)){return true;}return false;},_onButtonMouseDown:function(m){var A=this;m.halt();A._focus();A._sendQuery(A.inputNode.get("value")+"*");},_onInterval:function(){var A=this;var n=A.inputNode.get("value");var m=A._lastValue;if(n!=m){A._lastValue=n;A._sendQuery(n);}},_onTextboxBlur:function(m){var A=this;if(!A._overContainer||(A._keyCode==g)){A.fire("textboxBlur");}else{A._focus();}},_onTextboxFocus:function(m){var A=this;if(!A.get("focused")){A.inputNode.setAttribute("autocomplete","off");A.focus();A._initInputValue=A.inputNode.get("value");A.fire("textboxFocus");}},_onTextboxKeyDown:function(m){var A=this;var n=m.keyCode;if(A._typeAheadDelayId!=-1){clearTimeout(A._typeAheadDelayId);}A.fire("textboxKeyDown",m);if(n==D){A._enableIntervalDetection();}A._keyCode=n;},_onTextboxKeyPress:function(m){var A=this;var n=m.keyCode;A.fire("textboxKeyPress",m);if(n==E){A._enableIntervalDetection();}},_onTextboxKeyUp:function(m){var A=this;var n=m.keyCode;if(A._isIgnoreKey(n)){return;}A.fire("textboxKeyUp",m);},_renderInput:function(){var r=this;var n=r.get(V);var p=r.get("input");var A=r.get("iconButton")||O;var o={field:{labelText:false},icons:[{icon:A,id:"trigger",handler:{fn:r._onButtonMouseDown,context:r}}]};var s=null;var t=null;if(p){p=J.one(p);o.field.node=p;s=p.next();t=p.get("parentNode");}var q=new J.Combobox(o).render(n);if(t){var m=q.get("boundingBox");t.insertBefore(m,s);}r.inputNode=q.get("node");r.button=q.icons.item("trigger");r.comboBox=q;r.set("uniqueName",J.stamp(r.inputNode));},_sendQuery:function(q){var m=this;if(m.get("disabled")){m.fire("sendQueryDisabled",q);return;}var o=m.get("delimChar");var n=m.get("minQueryLength");if(o){var A=m._extractQuery(q);q=A.query;m._pastSelections=A.previous;}if((q&&(q.length<n))||(!q&&n>0)){m.fire("invalidQueryLength",q);return;}q=encodeURIComponent(q);var r=m.get("dataSource");var p=m.generateRequest(q);m.fire("dataRequest",p);r.sendRequest(p);},_typeAhead:function(m,n){var A=this;if(!A.get("typeAhead")||A._keyCode==M){return;}var o=J.Node.getDOMNode(A.inputNode);if(o.setSelectionRange||o.createTextRange){A._typeAheadDelayId=setTimeout(function(){var r=o.value;var s=r.length;A._updateValue(m);var p=o.value.length;A.inputNode.selectText(s,p);var q=o.value.substr(s,p);A.fire("typeAhead",n,q);},A.get("typeAheadDelay"));
}},_currentQuery:null,_initInputValue:null,_keyCode:null,_lastValue:null,_pastSelections:"",_typeAheadDelayId:-1};J.InputTextControl=i;},"1.0.1",{requires:["aui-base","aui-datasource-control-base","aui-form-combobox"]});