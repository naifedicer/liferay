AUI.add("aui-rating",function(Z){var U=Z.Lang,p=U.isBoolean,AL=U.isNumber,V=U.isString,g=function(A){return(A instanceof Z.NodeList);},T=function(A){return(A instanceof Z.Node);},r="a",a="",AH="boundingBox",s="canReset",I="clearfix",k="contentBox",o="defaultSelected",n="disabled",h=".",x="element",N="elements",u="",z="helper",c="hover",AA="href",J="javascript:;",i="id",Q="input",AC="inputName",AJ="label",AN="labelNode",q="name",AB="nodeName",X="off",d="on",b="rating",C="selectedIndex",K="showTitle",j="size",t="title",AF="value",B="itemClick",AM="itemSelect",D="itemOut",y="itemOver",P=Z.ClassNameManager.getClassName,m=P(z,I),AK=P(b,AJ,x),H=P(b,x),v=P(b,x,c),M=P(b,x,X),f=P(b,x,d),R='<div class="'+AK+'"></div>',G={tagName:"a",attrs:""},F={tagName:"a",attrs:'href="'+J+'"'},Y='<a href="'+J+'"></a>',S="<span></span>",E=' class="'+H+'"',w=function(L){var AO=L?F:G;var A=["<",AO.tagName,AO.attrs,E,">","</",AO.tagName,">"];return A.join("");};var W=Z.Component.create({NAME:"rating",ATTRS:{disabled:{value:false,validator:p},canReset:{value:true,validator:p},defaultSelected:{value:0,writeOnce:true,validator:AL},elements:{validator:g},hiddenInput:{validator:T},inputName:{value:a,validator:V},label:{value:a,validator:V},labelNode:{valueFn:function(){return Z.Node.create(R);},validator:T},selectedIndex:{value:-1,validator:AL},showTitle:{value:true,validator:p},size:{value:5,validator:function(A){return AL(A)&&(A>0);}},title:null,value:null},HTML_PARSER:{elements:function(A){return A.all(h+H);},label:function(L){var A=L.one(h+AK);if(A){return A.html();}},labelNode:h+AK},prototype:{initializer:function(){var A=this;A.inputElementsData={};A.after("labelChange",this._afterSetLabel);},renderUI:function(){var A=this;var L=A.get(k);L.addClass(m);A._parseInputElements();A._renderLabel();A._renderElements();},bindUI:function(){var A=this;A._createEvents();A.on("click",A._handleClickEvent);A.on("mouseover",A._handleMouseOverEvent);A.on("mouseout",A._handleMouseOutEvent);},syncUI:function(){var A=this;A._syncElements();A._syncLabelUI();},clearSelection:function(){var A=this;A.get(N).each(function(L){L.removeClass(f);L.removeClass(v);});},select:function(AP){var AU=this;var AQ=AU.get(C);var AS=AU.get(s);if(AS&&(AQ==AP)){AP=-1;}AU.set(C,AP);var L=AU.get(C);var AO=AU._getInputData(L);var AR=(t in AO)?AO.title:a;var AT=(AF in AO)?AO.value:L;AU.fillTo(L);AU.set(t,AR);AU.set(AF,AT);var A=AU.get("hiddenInput");A.setAttribute(t,AR);A.setAttribute(AF,AT);},fillTo:function(L,AO){var A=this;A.clearSelection();if(L>=0){A.get(N).some(function(AQ,AP){AQ.addClass(AO||f);return(AP==Math.floor(L));});}},indexOf:function(L){var A=this;return A.get(N).indexOf(L);},_canFireCustomEvent:function(L){var A=this;var AO=L.domEvent.target;return !A.get(n)&&AO.hasClass(H);},_createElements:function(){var L=this;var AR=[];var A=w(L.get(n));for(var AP=0,AO=this.get(j);AP<AO;AP++){AR.push(A);}var AQ=Z.DOM.create(AR.join(""));return new Z.NodeList(AQ.childNodes);},_createEvents:function(){var A=this;var L=function(AO,AP){A.publish(AO,{defaultFn:AP,queuable:false,emitFacade:true,bubbles:true});};L(B,this._defRatingItemClickFn);L(AM,this._defRatingItemSelectFn);L(y,this._defRatingItemOverFn);L(D,this._defRatingItemOutFn);},_defRatingItemClickFn:function(AO){var A=this;var L=AO.domEvent;A.fire(AM,{delegateEvent:AO,domEvent:L,ratingItem:L.target});},_defRatingItemSelectFn:function(L){var A=this;var AO=L.domEvent.target;A.select(A.indexOf(AO));},_defRatingItemOutFn:function(L){var A=this;A.fillTo(A.get(C));},_defRatingItemOverFn:function(AO){var A=this;var L=A.indexOf(AO.domEvent.target);A.fillTo(L,v);},_parseInputElements:function(){var A=this;var AP=A.get(AH);var L=AP.all(Q);var AQ=L.size();var AO=A.get(AC);var AR=Z.Node.create('<input type="hidden" />');if(AQ>0){AO=AO||L.item(0).getAttribute(q);A.set(j,AQ);var AS=AP.getElementsByTagName("label");L.each(function(AW,AV){var AX=AW.get(i);var AU=u;if(AX){var AT=AS.filter('[for="'+AX+'"]');if(AT.size()){AU=AT.item(0).html();}}A.inputElementsData[AV]={content:AU,value:AW.getAttribute(AF)||AV,title:AW.getAttribute(t)};});AS.remove(true);L.remove(true);}if(AO){AR.setAttribute(q,AO);AP.appendChild(AR);}A.set("hiddenInput",AR);},_renderLabel:function(){var A=this;A.get(k).append(A.get(AN));},_renderElements:function(AO){var A=this;var L=A.get(k);var AO=A.get(N);if(!AO.size()){AO=A._createElements();A.set(N,AO);}AO.each(function(AQ,AP){var AS=A._getInputData(AP);var AR=AS.content;var AT=AS.title||A.get(t)||AR;if(AR||AT){AQ.html(AR||AT);}if(AT&&A.get(K)){AQ.setAttribute(t,AT);}if(!AQ.attr(AA)&&(AQ.get(AB).toLowerCase()==r)){AQ.setAttribute(AA,J);}});L.append(AO.getDOM());},_syncElements:function(){var L=this;var A=L.get(o)-1;L.set(C,A);L.select();},_syncLabelUI:function(){var A=this;var L=A.get(AJ);A.get(AN).html(L);},_getInputData:function(L){var A=this;return A.inputElementsData[L]||{};},_handleClickEvent:function(L){var A=this;if(A._canFireCustomEvent(L)){A.fire(B,{delegateEvent:L,domEvent:L.domEvent});}},_handleMouseOutEvent:function(L){var A=this;if(A._canFireCustomEvent(L)){A.fire(D,{delegateEvent:L,domEvent:L.domEvent});}},_handleMouseOverEvent:function(L){var A=this;if(A._canFireCustomEvent(L)){A.fire(y,{delegateEvent:L,domEvent:L.domEvent});}},_afterSetLabel:function(A){this._syncLabelUI();}}});var l="down",AG="thumb",AI="ThumbRating",e="up",AE=P(b,AG,l),AD=P(b,AG,e);var O=Z.Component.create({NAME:AI,ATTRS:{size:{value:2,readOnly:true}},EXTENDS:W,prototype:{renderUI:function(){var A=this;O.superclass.renderUI.apply(this,arguments);var L=A.get(N);L.addClass(M);L.item(0).addClass(AD);L.item(1).addClass(AE);},fillTo:function(A,L){this.clearSelection();if(A>=0){this.get(N).item(A).addClass(L||f);}},_syncElements:function(){}}});Z.Rating=W;Z.StarRating=W;Z.ThumbRating=O;},"1.0.1",{skinnable:true,requires:["aui-base"]});