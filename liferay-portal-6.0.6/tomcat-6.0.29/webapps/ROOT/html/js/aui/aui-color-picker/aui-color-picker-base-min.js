AUI.add("aui-color-picker-base",function(S){var G=S.Lang,N=G.isArray,L=G.isObject,d=S.ColorUtil,H="colorpicker",J=S.ClassNameManager.getClassName,c=S.WidgetStdMod,I=J(H,"canvas"),e=J(H,"hue-canvas"),Q=J(H,"container"),T=J(H,"controls"),M=J(H,"panel"),R=J(H,"swatch"),D=J(H,"swatch-current"),Y=J(H,"swatch-original"),U=J(H,"thumb"),O=J(H,"thumb-image"),K=J(H,"hue-thumb"),E=J(H,"hue-thumb-image"),Z=J(H,"hue","slider"),X=J(H,"hue","slider","content"),F=J(H,"trigger"),a='<div class="'+I+'"></div>',W='<span class="'+e+'"></span>',P='<div class="'+R+'"></div>',f='<div class="'+D+'"></div>',C='<div class="'+Y+'"></div>',V='<div class="'+U+'"><div class="'+O+'"></div></div>',b='<span class="'+K+'"><span class="'+E+'"></span></span>';var B=S.Component.create({NAME:H,ATTRS:{colors:{value:{},getter:function(){var A=this;var h=A.get("rgb");var i=A.get("hex");var g={};S.mix(g,h);g.hex=i;return g;}},hex:{value:"FFFFFF",getter:function(){var A=this;var g=A.get("rgb");var h=g.hex;if(h){h=h.split("#").join("");}else{h=d.rgb2hex(g);}return h;},setter:function(h){var A=this;if(h){var g=d.getRGB("#"+h);h=g.hex.split("#").join("");A.set("rgb",g);}else{h=S.Attribute.INVALID_VALUE;}return h;}},hideOn:{value:"click"},hsv:{getter:function(h){var A=this;var g=A.get("rgb");return d.rgb2hsv(g);},setter:function(h){var A=this;if(N(h)){var i=A.get("hsv");var g=d.hsv2rgb(h);A.set("rgb",g);i={hue:h[0],saturation:h[1],value:[2]};}else{if(!L(h)){h=S.Attribute.INVALID_VALUE;}}return h;},value:{h:0,s:0,v:0}},showOn:{value:"click"},pickersize:{value:180},rgb:{value:new d.RGB(255,255,255),setter:function(k){var h=this;var j;var i;var A;var l=true;if(N(k)){j=k[0];i=k[0];A=k[0];}else{if(L){j=k.r;i=k.g;A=k.b;}else{k=S.Attribute.INVALID_VALUE;l=false;}}if(l){j=d.constrainTo(j,0,255,255);i=d.constrainTo(i,0,255,255);A=d.constrainTo(A,0,255,255);k=new d.RGB(j,i,A);}return k;}},strings:{value:{R:"R",G:"G",B:"B",H:"H",S:"S",V:"V",HEX:"#",DEG:"\u00B0",PERCENT:"%"}},triggerParent:{value:null},trigger:{lazyAdd:true,getter:function(g){var A=this;if(!g){A._buttonTrigger=new S.ButtonItem({cssClass:F,icon:"pencil"});g=A._buttonTrigger.get("boundingBox");A.set("trigger",g);}return g;}}},EXTENDS:S.OverlayContext,prototype:{renderUI:function(){var A=this;var h=A._buttonTrigger;if(h&&!h.get("rendered")){var g=A.get("triggerParent");if(!g){g=A.get("boundingBox").get("parentNode");}h.render(g);}A._renderContainer();A._renderSliders();A._renderControls();},bindUI:function(){var A=this;B.superclass.bindUI.apply(this,arguments);A._createEvents();A._colorCanvas.on("mousedown",A._onCanvasMouseDown,A);A._colorPicker.on("drag:start",A._onThumbDragStart,A);A._colorPicker.after("drag:drag",A._afterThumbDrag,A);A._hueSlider.after("valueChange",A._afterValueChange,A);var g=A._colorForm.get("contentBox");g.delegate("change",S.bind(A._onFormChange,A),"input");A.after("hexChange",A._updateRGB);A.after("rgbChange",A._updateRGB);A._colorSwatchOriginal.on("click",A._restoreRGB,A);A.after("visibleChange",A._afterVisibleChangeCP);},syncUI:function(){var A=this;A._updatePickerOffset();var g=A.get("rgb");A._updateControls();A._updateOriginalRGB();},_afterThumbDrag:function(g){var A=this;var h=A._translateOffset(g.pageX,g.pageY);if(!A._preventDragEvent){A.fire("colorChange",{ddEvent:g});}A._canvasThumbXY=h;},_afterValueChange:function(g){var A=this;if(g.src!="controls"){A.fire("colorChange",{slideEvent:g});}},_afterVisibleChangeCP:function(g){var A=this;if(g.newVal){A.refreshAlign();A._hueSlider.syncUI();}A._updateOriginalRGB();},_convertOffsetToValue:function(g,i){var A=this;if(N(g)){return A._convertOffsetToValue.apply(A,g);}var h=A.get("pickersize");g=Math.round(((g*h/100)));i=Math.round((h-(i*h/100)));return[g,i];},_convertValueToOffset:function(g,h){var A=this;if(N(g)){return A._convertValueToOffset.apply(A,g);}g=Math.round(g+A._offsetXY[0]);h=Math.round(h+A._offsetXY[1]);return[g,h];},_createEvents:function(){var A=this;A.publish("colorChange",{defaultFn:A._onColorChange});},_getHuePicker:function(){var A=this;var h=A.get("pickersize");var g=(h-A._hueSlider.get("value"))/h;g=d.constrainTo(g,0,1,0);return(g===1)?0:g;},_getPickerSize:function(){var A=this;if(!A._pickerSize){var g=A._colorCanvas;var h=g.get("offsetWidth");if(!h){h=g.getComputedStyle("width");}h=parseInt(h,10);var i=A._pickerThumb.get("offsetWidth");h-=i;A._pickerSize=h;}return A._pickerSize;},_getSaturationPicker:function(){var A=this;return A._canvasThumbXY[0]/A._getPickerSize();},_getThumbOffset:function(){var g=this;if(!g._thumbOffset){var h=g._pickerThumb;var A=h.get("offsetHeight");var i=h.get("offsetWidth");g._thumbOffset=[Math.floor(i/2),Math.floor(A/2)];}return g._thumbOffset;},_getValuePicker:function(){var A=this;var g=A._getPickerSize();return((g-A._canvasThumbXY[1]))/g;},_onCanvasMouseDown:function(g){var A=this;A._setDragStart(g.pageX,g.pageY);g.halt();A.fire("colorChange",{ddEvent:g});},_onColorChange:function(j){var A=this;var g=A._getHuePicker();var i=A._getSaturationPicker();var k=A._getValuePicker();var h=d.hsv2rgb(g,i,k);if(j.src!="controls"){A.set("rgb",h);}A._updateControls();if(!j.ddEvent){if(!j.slideEvent){A._updateHue();A._updatePickerThumb();g=A._getHuePicker();}var l=d.hsv2rgb(g,1,1);A._updateCanvas(l);}A._updateColorSwatch();},_onFormChange:function(h){var A=this;var g=h.currentTarget;var i=g.get("id");if(i!="hex"){i="rgb."+i;}A.set(i,g.val());},_onThumbDragStart:function(g){var A=this;A._updatePickerOffset();},_renderContainer:function(){var A=this;if(!A._pickerContainer){var g=new S.Panel({cssClass:M,icons:[{icon:"close",id:"close",handler:{fn:A.hide,context:A}}]}).render(A.get("contentBox"));var h=g.bodyNode;h.addClass(Q);A._pickerContainer=h;}},_renderControls:function(){var g=this;g._colorSwatch=S.Node.create(P);g._colorSwatchCurrent=S.Node.create(f);g._colorSwatchOriginal=S.Node.create(C);g._colorSwatch.appendChild(g._colorSwatchCurrent);g._colorSwatch.appendChild(g._colorSwatchOriginal);g._pickerContainer.appendChild(g._colorSwatch);var A=g.get("strings");var h=new S.Form({labelAlign:"left"}).render(g._pickerContainer);
h.add([{id:"r",labelText:A.R,size:3},{id:"g",labelText:A.G,size:3},{id:"b",labelText:A.B,size:3},{id:"hex",labelText:A.HEX,size:6}],true);h.get("boundingBox").addClass(T);g._colorForm=h;},_renderSliders:function(){var A=this;A._colorCanvas=S.Node.create(a);A._pickerThumb=S.Node.create(V);A._colorCanvas.appendChild(A._pickerThumb);A._pickerContainer.appendChild(A._colorCanvas);var g=A.get("pickersize");A._colorPicker=new S.DD.Drag({node:A._pickerThumb}).plug(S.Plugin.DDConstrained,{constrain2node:A._colorCanvas});var h=new S.Slider({axis:"y",min:0,max:g,length:A._colorCanvas.get("offsetHeight")});h.RAIL_TEMPLATE=W;h.THUMB_TEMPLATE=b;h.get("boundingBox").addClass(Z);h.get("contentBox").addClass(X);h.render(A._pickerContainer);A._hueSlider=h;},_restoreRGB:function(g){var A=this;A.set("rgb",A._oldRGB);A._updateHue();A._updatePickerThumb();A._updateColorSwatch();A.fire("colorChange");},_setDragStart:function(h,k){var g=this;if(N(h)){return g._setDragStart.apply(g,h);}var A=g._colorPicker;A._dragThreshMet=true;A._fixIEMouseDown();S.DD.DDM.activeDrag=A;var j=A.get("dragNode").getXY();var i=g._getThumbOffset();j[0]+=i[0];j[1]+=i[1];A._setStartPosition(j);A.set("activeHandle",A.get("dragNode"));A.start();A._alignNode([h,k]);},_translateOffset:function(g,j){var A=this;var h=A._offsetXY;var i=[];i[0]=Math.round(g-h[0]);i[1]=Math.round(j-h[1]);return i;},_updateCanvas:function(g){var A=this;g=g||A.get("rgb");A._colorCanvas.setStyle("backgroundColor","rgb("+[g.r,g.g,g.b].join(",")+")");},_updateColorSwatch:function(g){var A=this;g=g||A.get("rgb");A._colorSwatchCurrent.setStyle("backgroundColor","rgb("+[g.r,g.g,g.b].join(",")+")");},_updateControls:function(){var A=this;var g=A.get("colors");A._colorForm.set("values",g);},_updateHue:function(){var A=this;var h=A.get("pickersize");var g=A.get("hsv.h");g=h-Math.round(g*h);if(g===h){g=0;}A._hueSlider.set("value",g,{src:"controls"});},_updateOriginalColorSwatch:function(g){var A=this;g=g||A.get("rgb");A._colorSwatchOriginal.setStyle("backgroundColor","rgb("+[g.r,g.g,g.b].join(",")+")");},_updateOriginalRGB:function(){var A=this;A._oldRGB=A.get("rgb");A._updateOriginalColorSwatch(A._oldRGB);},_updatePickerOffset:function(){var A=this;A._offsetXY=A._colorCanvas.getXY();},_updatePickerThumb:function(){var g=this;g._updatePickerOffset();var h=g.get("hsv");var i=g.get("pickersize");h.s=Math.round(h.s*100);var j=h.s;h.v=Math.round(h.v*100);var k=h.v;var l=g._convertOffsetToValue(j,k);l=g._convertValueToOffset(l);g._canvasThumbXY=l;var A=g._colorPicker;g._preventDragEvent=true;A._setStartPosition(A.get("dragNode").getXY());A._alignNode(l,true);g._preventDragEvent=false;},_updateRGB:function(g){var A=this;if(g.subAttrName||g.attrName=="hex"){A.fire("colorChange",{src:"controls"});}},_canvasThumbXY:[0,0],_offsetXY:[0,0]}});S.ColorPicker=B;},"1.0.1",{skinnable:true,requires:["aui-overlay-context","dd-drag","slider","substitute","aui-button-item","aui-color-util","aui-form-base","aui-panel"]});