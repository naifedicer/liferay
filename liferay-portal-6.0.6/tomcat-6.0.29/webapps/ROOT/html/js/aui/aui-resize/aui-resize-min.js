AUI.add("aui-resize-base",function(au){var ae=au.Lang,d=ae.isArray,aK=ae.isBoolean,aV=ae.isNull,aP=ae.isString,aG=ae.trim,o=au.Array.indexOf,al=".",w=",",u=" ",t="active",Q="activeHandle",X="activeHandleEl",C="all",ax="autoHide",aD="bottom",ay="className",aB="cursor",n="diagonal",aC="dotted",an="dragCursor",b="grip",af="gripsmall",E="handle",P="handles",U="handlesWrapper",ak="hidden",a="horizontal",ai="icon",z="inner",c="left",O="margin",s="node",H="nodeName",ab="none",K="offsetHeight",aO="offsetWidth",f="parentNode",p="position",aF="proxy",D="proxyEl",l="relative",av="resize",r="resizing",i="right",aT="static",k="top",ar="vertical",az="wrap",aQ="wrapper",aq="wrapTypes",M="resize:mouseUp",x="resize:resize",F="resize:align",N="resize:end",Y="resize:start",ag="t",aS="tr",ah="r",aI="br",at="b",aM="bl",am="l",aU="tl",V=function(A){return(A instanceof au.Node);},aW=function(A){return E+A.toUpperCase();},aR=function(){return Array.prototype.slice.call(arguments).join(u);},W=au.cached(function(A){return A.substring(0,1).toUpperCase()+A.substring(1);}),G=au.ClassNameManager.getClassName,m=G(ai),aJ=G(ai,af,n,aI),ap=G(ai,b,aC,a),aa=G(ai,b,aC,ar),aH=G(av),aA=G(av,E),aj=G(av,E,t),g=G(av,E,z),I=G(av,E,z,"{handle}"),aX=G(av,E,"{handle}"),e=G(av,ak,P),y=G(av,aF),aN=G(av,aQ),ad=G(av,P,aQ),ao=aR(m,aJ),aE=aR(m,ap),q=aR(m,aa),h=/^(t|tr|b|bl|br|tl)$/i,Z=/^(tl|l|bl)$/i,S=/^(tl|t|tr)$/i,J=/^(bl|br|l|r|tl|tr)$/i,aw='<div class="'+aR(aA,aX)+'">'+'<div class="'+aR(g,I)+'"></div>'+"</div>",ac='<div class="'+y+'"></div>',aL='<div class="'+ad+'"></div>',aY='<div class="'+aN+'"></div>',v=[ag,aS,ah,aI,at,aM,am,aU];var j=au.Component.create({NAME:av,ATTRS:{activeHandle:{value:null,validator:function(A){return aP(A)||aV(A);}},activeHandleEl:{value:null,validator:V},autoHide:{value:false,validator:aK},handles:{setter:function(L){var A=this;var B=[];if(d(L)){B=L;}else{if(aP(L)){if(L.toLowerCase()==C){B=v;}else{au.each(L.split(w),function(T,R){var aZ=aG(T);if(o(v,aZ)>-1){B.push(aZ);}});}}}return B;},value:C},handlesWrapper:{setter:au.one,valueFn:function(){return au.Node.create(aL);}},node:{setter:au.one},proxy:{value:false,validator:aK},proxyEl:{setter:au.one,valueFn:function(){return au.Node.create(ac);}},resizing:{value:false,validator:aK},wrap:{setter:function(R){var A=this;var L=A.get(s);var T=L.get(H);var B=A.get(aq);if(B.test(T)){R=true;}return R;},value:false,validator:aK},wrapTypes:{readOnly:true,value:/canvas|textarea|input|select|button|img/i},wrapper:{setter:function(){var A=this;var B=A.get(s);var L=B;if(A.get(az)){L=au.Node.create(aY);B.placeBefore(L);L.append(B);A._copyStyles(B,L);B.setStyles({position:aT,left:0,top:0});}return L;},value:null,writeOnce:true}},EXTENDS:au.Base,prototype:{CSS_INNER_HANDLE_MAP:{r:q,l:q,t:aE,b:aE,br:ao},changeHeightHandles:false,changeLeftHandles:false,changeTopHandles:false,changeWidthHandles:false,delegate:null,info:null,lastInfo:null,originalInfo:null,initializer:function(){var A=this;A.info={};A.originalInfo={};A.get(s).addClass(aH);A.renderer();},renderUI:function(){var A=this;A._renderHandles();A._renderProxy();},bindUI:function(){var A=this;A._createEvents();A._bindDD();A._bindHandle();},syncUI:function(){var A=this;A._setHideHandlesUI(A.get(ax));},destructor:function(){var A=this;var B=A.get(s);var L=A.get(aQ);au.Event.purgeElement(L,true);A.eachHandle(function(R){A.delegate.dd.destroy();R.remove(true);});if(A.get(az)){B.setStyles({margin:L.getStyle(O),position:L.getStyle(p)});L.placeBefore(B);L.remove(true);}B.removeClass(aH);B.removeClass(e);},renderer:function(){this.renderUI();this.bindUI();this.syncUI();},eachHandle:function(B){var A=this;au.each(A.get(P),function(T,L){var R=A.get(aW(T));B.apply(A,[R,T,L]);});},_bindDD:function(){var A=this;A.delegate=new au.DD.Delegate({bubbleTargets:A,container:A.get(U),dragConfig:{clickPixelThresh:0,clickTimeThresh:0,useShim:true,move:false},nodes:al+aA,target:false});A.on("drag:drag",A._handleResizeEvent);A.on("drag:dropmiss",A._handleMouseUpEvent);A.on("drag:end",A._handleResizeEndEvent);A.on("drag:start",A._handleResizeStartEvent);},_bindHandle:function(){var A=this;var B=A.get(aQ);B.on("mouseenter",au.bind(A._onWrapperMouseEnter,A));B.on("mouseleave",au.bind(A._onWrapperMouseLeave,A));B.delegate("mouseenter",au.bind(A._onHandleMouseEnter,A),al+aA);B.delegate("mouseleave",au.bind(A._onHandleMouseLeave,A),al+aA);},_createEvents:function(){var A=this;var B=function(L,R){A.publish(L,{defaultFn:R,queuable:false,emitFacade:true,bubbles:true,prefix:av});};B(Y,this._defResizeStartFn);B(x,this._defResizeFn);B(F,this._defResizeAlignFn);B(N,this._defResizeEndFn);B(M,this._defMouseUpFn);},_renderHandles:function(){var A=this;var L=A.get(aQ);var B=A.get(U);A.eachHandle(function(R){B.append(R);});L.append(B);},_renderProxy:function(){var B=this;var A=B.get(D);B.get(aQ).get(f).append(A.hide());},_buildHandle:function(L){var A=this;var B=au.Node.create(au.substitute(aw,{handle:L}));B.one(al+g).addClass(A.CSS_INNER_HANDLE_MAP[L]);return B;},_checkSize:function(aZ,B){var A=this;var T=A.info;var R=A.originalInfo;var L=(aZ==K)?k:c;T[aZ]=B;if(((L==c)&&A.changeLeftHandles)||((L==k)&&A.changeTopHandles)){T[L]=R[L]+R[aZ]-B;}},_copyStyles:function(T,aZ){var B=this;var A=T.getStyle(p).toLowerCase();if(A==aT){A=l;}var R={position:A};var L={};au.each([k,i,aD,c],function(a1){var a0=O+W(a1);L[a0]=aZ.getStyle(a0);R[a0]=T.getStyle(a0);});aZ.setStyles(R);T.setStyles(L);T.setStyles({margin:0});aZ.set(K,T.get(K));aZ.set(aO,T.get(aO));},_extractHandleName:au.cached(function(L){var B=L.get(ay);var A=B.match(new RegExp(G(av,E,"(\\w{1,2})\\b")));return A?A[1]:null;}),_getInfo:function(T,A){var a3=this,aZ;var a1=A.dragEvent.target;if(A){aZ=(a1.actXY.length?a1.actXY:a1.lastXY);}var a0=T.getXY();var R=a0[0];var L=a0[1];var B=T.get(K);var a2=T.get(aO);return{actXY:aZ,bottom:(L+B),left:R,offsetHeight:B,offsetWidth:a2,right:(R+a2),top:L};},_resize:function(){var A=this;var R=A.get(Q);var aZ=A.info;var T=A.originalInfo;var L=aZ.actXY[0]-T.actXY[0];var B=aZ.actXY[1]-T.actXY[1];
var a0={t:function(){aZ.top=T.top+B;aZ.offsetHeight=T.offsetHeight-B;},r:function(){aZ.offsetWidth=T.offsetWidth+L;},l:function(){aZ.left=T.left+L;aZ.offsetWidth=T.offsetWidth-L;},b:function(){aZ.offsetHeight=T.offsetHeight+B;},tr:function(){this.t();this.r();},br:function(){this.b();this.r();},tl:function(){this.t();this.l();},bl:function(){this.b();this.l();}};if(R&&a0[R]){a0[R](L,B);}},_setOffset:function(L,B,A){L.set(aO,B);L.set(K,A);},_syncUI:function(){var A=this;var L=A.info;var R=A.get(aQ);var B=A.get(s);A._setOffset(R,L.offsetWidth,L.offsetHeight);if(A.changeLeftHandles||A.changeTopHandles){R.setXY([L.left,L.top]);}if(!R.compareTo(B)){A._setOffset(B,L.offsetWidth,L.offsetHeight);}if(au.UA.webkit){B.setStyle(av,ab);}},_syncProxyUI:function(){var B=this;var R=B.info;var L=B.get(X);var A=B.get(D);var T=L.getStyle(aB);A.show().setStyle(aB,T);B.delegate.dd.set(an,T);B._setOffset(A,R.offsetWidth,R.offsetHeight);A.setXY([R.left,R.top]);},_updateChangeHandleInfo:function(B){var A=this;A.changeHeightHandles=h.test(B);A.changeLeftHandles=Z.test(B);A.changeTopHandles=S.test(B);A.changeWidthHandles=J.test(B);},_updateInfo:function(B){var A=this;A.info=A._getInfo(A.get(aQ),B);},_setActiveHandlesUI:function(L){var A=this;var B=A.get(X);if(B){if(L){A.eachHandle(function(R){R.removeClass(aj);});B.addClass(aj);}else{B.removeClass(aj);}}},_setHideHandlesUI:function(B){var A=this;var L=A.get(aQ);if(!A.get(r)){if(B){L.addClass(e);}else{L.removeClass(e);}}},_defMouseUpFn:function(B){var A=this;A.set(r,false);},_defResizeFn:function(B){var A=this;A._handleResizeAlignEvent(B.dragEvent);if(A.get(aF)){A._syncProxyUI();}else{A._syncUI();}},_defResizeAlignFn:function(B){var A=this;var R=A.originalInfo;A.lastInfo=A.info;A._updateInfo(B);var L=A.info;A._resize();if(!A.con){if(L.offsetHeight<=15){A._checkSize(K,15);}if(L.offsetWidth<=15){A._checkSize(aO,15);}}},_defResizeEndFn:function(L){var A=this;var B=L.dragEvent.target;B.actXY=[];if(A.get(aF)){A._syncProxyUI();A.get(D).hide();}A._syncUI();A._setActiveHandlesUI(false);A.set(Q,null);A.set(X,null);},_defResizeStartFn:function(B){var A=this;A.set(r,true);A.originalInfo=A._getInfo(A.get(aQ),B);A._updateInfo(B);},_handleMouseUpEvent:function(A){this.fire(M,{dragEvent:A,info:this.info});},_handleResizeEvent:function(A){this.fire(x,{dragEvent:A,info:this.info});},_handleResizeAlignEvent:function(A){this.fire(F,{dragEvent:A,info:this.info});},_handleResizeEndEvent:function(A){this.fire(N,{dragEvent:A,info:this.info});},_handleResizeStartEvent:function(A){if(!this.get(Q)){this._setHandleFromNode(A.target.get("node"));}this.fire(Y,{dragEvent:A,info:this.info});},_onWrapperMouseEnter:function(B){var A=this;if(A.get(ax)){A._setHideHandlesUI(false);}},_onWrapperMouseLeave:function(B){var A=this;if(A.get(ax)){A._setHideHandlesUI(true);}},_setHandleFromNode:function(B){var A=this,L=A._extractHandleName(B);if(!A.get(r)){A.set(Q,L);A.set(X,B);A._setActiveHandlesUI(true);A._updateChangeHandleInfo(L);}},_onHandleMouseEnter:function(B){var A=this;A._setHandleFromNode(B.currentTarget);},_onHandleMouseLeave:function(B){var A=this;if(!A.get(r)){A._setActiveHandlesUI(false);}}}});au.each(v,function(B,A){j.ATTRS[aW(B)]={setter:function(){return this._buildHandle(B);},value:null,writeOnce:true};});au.Resize=j;},"1.0.1",{requires:["aui-base","dd-drag","dd-delegate","dd-drop","substitute"],skinnable:true});AUI.add("aui-resize-constrain",function(t){var i=t.Lang,o=i.isBoolean,s=i.isNumber,q=i.isString,z=function(A){return(A instanceof t.Node);},D="borderBottomWidth",f="borderLeftWidth",y="borderRightWidth",j="borderTopWidth",m="bottom",l="con",E="constrain",F="host",r="left",h="maxHeight",v="maxWidth",a="minHeight",k="minWidth",G="node",b="offsetHeight",n="offsetWidth",d="preserveRatio",x="region",u="resizeConstrained",p="right",e="tickX",c="tickY",w="top",C="view",B="viewportRegion";function g(){g.superclass.constructor.apply(this,arguments);}t.mix(g,{NAME:u,NS:l,ATTRS:{constrain:{setter:function(A){if(A&&(z(A)||q(A)||A.nodeType)){A=t.one(A);}return A;}},minHeight:{value:15,validator:s},minWidth:{value:15,validator:s},maxHeight:{value:Infinity,validator:s},maxWidth:{value:Infinity,validator:s},preserveRatio:{value:false,validator:o},tickX:{value:false},tickY:{value:false}}});t.extend(g,t.Plugin.Base,{constrainBorderInfo:null,initializer:function(){var A=this;var H=A.get(F);A.constrainBorderInfo={bottom:0,left:0,right:0,top:0};H.delegate.dd.plug(t.Plugin.DDConstrained,{tickX:A.get(e),tickY:A.get(c)});H.after("resize:align",t.bind(A._handleResizeAlignEvent,A));H.on("resize:start",t.bind(A._handleResizeStartEvent,A));},_checkConstrain:function(H,P,I){var N=this;var Q=N.get(F);var A=Q.info;var L=N._getConstrainRegion();if(L){var M=A[H]+A[I];var J=L[P]-N.constrainBorderInfo[P];if(M>=J){A[I]-=(M-J);}var K=A[H];var O=L[H]+N.constrainBorderInfo[H];if(K<=O){A[H]+=(O-K);A[I]-=(O-K);}}},_checkHeight:function(){var A=this;var I=A.get(F);var L=I.info;var K=I.originalInfo;var H=A.get(h);var J=A.get(a);A._checkConstrain(w,m,b);if(L.offsetHeight>H){I._checkSize(b,H);}if(L.offsetHeight<J){I._checkSize(b,J);}},_checkRatio:function(){var U=this;var N=U.get(F);var T=N.info;var J=N.originalInfo;var M=J.offsetWidth;var V=J.offsetHeight;var L=J.top;var W=J.left;var P=J.bottom;var S=J.right;var I=function(){return(T.offsetWidth/M);};var K=function(){return(T.offsetHeight/V);};var O=N.changeHeightHandles;if(U.get(E)&&N.changeHeightHandles&&N.changeWidthHandles){var Q=U._getConstrainRegion();var X=U.constrainBorderInfo;var A=(Q.bottom-X.bottom)-P;var R=W-(Q.left+X.left);var H=(Q.right-X.right)-S;var Y=L-(Q.top+X.top);if(N.changeLeftHandles&&N.changeTopHandles){O=(Y<R);}else{if(N.changeLeftHandles){O=(A<R);}else{if(N.changeTopHandles){O=(Y<H);}else{O=(A<H);}}}}if(O){T.offsetWidth=M*K();U._checkWidth();T.offsetHeight=V*I();}else{T.offsetHeight=V*I();U._checkHeight();T.offsetWidth=M*K();}if(N.changeTopHandles){T.top=L+(V-T.offsetHeight);}if(N.changeLeftHandles){T.left=W+(M-T.offsetWidth);}t.each(T,function(aa,Z){if(s(aa)){T[Z]=Math.round(aa);
}});},_checkRegion:function(){var A=this;var H=A.get(F);var I=A._getConstrainRegion();return t.DOM.inRegion(null,I,true,H.info);},_checkWidth:function(){var A=this;var J=A.get(F);var L=J.info;var K=J.originalInfo;var I=A.get(v);var H=A.get(k);A._checkConstrain(r,p,n);if(L.offsetWidth<H){J._checkSize(n,H);}if(L.offsetWidth>I){J._checkSize(n,I);}},_getConstrainRegion:function(){var A=this;var I=A.get(F);var H=I.get(G);var K=A.get(E);var J=null;if(K){if(K==C){J=H.get(B);}else{if(z(K)){J=K.get(x);}else{J=K;}}}return J;},_handleResizeAlignEvent:function(I){var A=this;var H=A.get(F);A._checkHeight();A._checkWidth();if(A.get(d)){A._checkRatio();}if(A.get(E)&&!A._checkRegion()){H.info=H.lastInfo;}},_handleResizeStartEvent:function(H){var A=this;A._updateConstrainBorderInfo();},_updateConstrainBorderInfo:function(){var H=this;var I=H.get(E);if(z(I)){var A=function(J){return parseFloat(I.getStyle(J))||0;};H.constrainBorderInfo.bottom=A(D);H.constrainBorderInfo.left=A(f);H.constrainBorderInfo.right=A(y);H.constrainBorderInfo.top=A(j);}}});t.namespace("Plugin");t.Plugin.ResizeConstrained=g;},"1.0.1",{requires:["aui-resize-base","dd-constrain","plugin"],skinnable:false});AUI.add("aui-resize",function(a){},"1.0.1",{use:["aui-resize-base","aui-resize-constrain"],skinnable:true});