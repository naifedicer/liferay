AUI.add("aui-drawing-base",function(O){var q=O.Lang,T=q.isArray,Q=q.isFunction,o=q.isNumber,e=q.isObject,J=q.isString,R=q.isUndefined,Z=O.config.doc,X=O.ColorUtil,B,V=O.UA,f=q.emptyFn,N={blur:0,"clip-rect":"0 0 1e9 1e9",cursor:"default",cx:0,cy:0,fill:"#fff","fill-opacity":1,font:'10px "Arial"',"font-family":'"Arial"',"font-size":"10","font-style":"normal","font-weight":400,gradient:0,height:0,href:"http://alloy.liferay.com/",opacity:1,path:"M0,0",r:0,rotation:0,rx:0,ry:0,scale:"1 1",src:"",stroke:"#000","stroke-dasharray":"","stroke-linecap":"butt","stroke-linejoin":"butt","stroke-miterlimit":0,"stroke-opacity":1,"stroke-width":1,target:"_blank","text-anchor":"middle",title:"AlloyUI",translation:"0 0",width:0,x:0,y:0},p={Q:1,T:1},C={circle:1,ellipse:1,image:1,path:1,rect:1,text:1},b={image:1,text:1},S={circle:1,ellipse:1},l=Math,g=l.max,L=l.min,k=l.pow,Y="drawing",U=/^([^:]*):?([\d\.]*)/,c=/^url\(['"]?([^\)]+?)['"]?\)$/i,P=/([achlmqstvz])[\s,]*((-?\d*\.?\d*(?:e[-+]?\d+)?\s*,?\s*)+)/ig,I=/,?([achlmqrstvxz]),?/gi,G=/(-?\d*\.?\d*(?:e[-+]?\d+)?)\s*,?\s*/ig,j=/^r(?:\(([^,]+?)\s*,\s*([^\)]+?)\))?/,n=/[, ]+/,K=/\s*\-\s*/,M="",d="fill",m=" progid:DXImageTransform.Microsoft",E=" ",i=String.prototype.toLowerCase,W=String.prototype.toUpperCase;var D=O.Component.create({ATTRS:{height:{value:342},width:{value:512},x:{value:null},y:{value:null}},UI_ATTRS:["x","y"],create:function(s){var r=arguments;var A=r[0];if(T(A)){r=r[0];s=B.getConfig(r);var w=new D(s).render();var x=w.createSet();var u;for(var t=0,v=r.length;t<v;t++){u=r[t]||{};if(C.hasOwnProperty(u.type)){x.push(w[u.type]().attr(u));}}return w;}else{if(e(A)&&!(A.nodeName||A instanceof O.Node)){s=A;}else{s=B.getConfig(r);}}return new D(s).render();},NAME:Y,prototype:{CONTENT_TEMPLATE:null,renderUI:function(){var A=this;A.customAttributes={};D.Impl.createCanvas.call(A);},circle:function(s,u,t){var A=this;return D.Impl.createCircle.call(A,s||0,u||0,t||0);},clear:function(){var A=this;return D.Impl.clear.call(A);},createSet:function(r){var A=this;if(arguments.length>1){r=Array.prototype.splice.call(arguments,0,arguments.length);}return new F(r);},ellipse:function(r,u,t,s){var A=this;return D.Impl.createEllipse.call(A,r||0,u||0,t||0,s||0);},image:function(u,r,v,s,t){var A=this;return D.Impl.createImage.call(A,u||"about:blank",r||0,v||0,s||0,t||0);},path:function(r){var A=this;if(J(r)){r=O.substitute.apply(O,arguments);}else{if(!R(r)&&!T(r)){r+=M;}}return D.Impl.createPath.call(A,r);},rect:function(s,z,t,u,v){var A=this;return D.Impl.createRectangle.call(A,s||0,z||0,t||0,u||0,v||0);},remove:function(){var A=this;return D.Impl.remove.call(A);},safari:f,text:function(r,t,s){var A=this;return D.Impl.createText.call(A,r||0,t||0,s||M);},_uiSetX:function(r){var A=this;A.get("boundingBox").setXY([r,A.get("y")]);},_uiSetY:function(r){var A=this;A.get("boundingBox").setXY([A.get("x"),r]);}}});B={getConfig:function(u){var t={};var r=u[0];var A="boundingBox";var w=2;var v=1;var s=-1;if(o(r)){A="x";w=3;v=2;s=1;}t[A]=r;t.height=u[w];t.width=u[v];t.y=u[s];return t;},translate:function(A,s){if(A==null){return{x:this._.tx,y:this._.ty,toString:B.XYToString};}this._.tx+=+A;this._.ty+=+s;switch(this.type){case"circle":case"ellipse":this.attr({cx:+A+this.attrs.cx,cy:+s+this.attrs.cy});break;case"rect":case"image":case"text":this.attr({x:+A+this.attrs.x,y:+s+this.attrs.y});break;case"path":var r=B.pathToRelative(this.attrs.path);r[0][1]+=+A;r[0][2]+=+s;this.attr({path:r});break;}return this;}};D.getColor=function(r){var s=D.getColor.start=D.getColor.start||{h:0,s:1,b:r||0.75};var A=X.hsb2rgb(s.h,s.s,s.b);s.h+=0.075;if(s.h>1){s.h=0;s.s-=0.2;if(s.s<=0){D.getColor.start={h:0,s:1,b:s.b};}}return A.hex;};D.getColor.reset=function(){delete D.start;};B.tear=function(r,A){r==A.top&&(A.top=r.prev);r==A.bottom&&(A.bottom=r.next);r.next&&(r.next.prev=r.prev);r.prev&&(r.prev.next=r.next);};B.toFront=function(r,A){if(A.top===r){return;}B.tear(r,A);r.next=null;r.prev=A.top;A.top.next=r;A.top=r;};B.toBack=function(r,A){if(A.bottom===r){return;}B.tear(r,A);r.next=A.bottom;r.prev=null;A.bottom.prev=r;A.bottom=r;};B.insertAfter=function(s,r,A){B.tear(s,A);r==A.top&&(A.top=s);r.next&&(r.next.prev=s);s.next=r.next;s.prev=r;r.next=s;};B.insertBefore=function(s,r,A){B.tear(s,A);r==A.bottom&&(A.bottom=s);r.prev&&(r.prev.next=s);s.prev=r.prev;r.prev=s;s.next=r;};var H=function(s,r){var A=this;A.createElement.apply(A,arguments);};B.removed=function(A){return function(){throw new Error('AlloyUI: you are calling to method "'+A+'" of removed object');};};B._path2string=function(){return this.join(",").replace(I,"$1");};B.parsePathString=O.cached(function(A){if(!A){return null;}var s={a:7,c:6,h:1,l:2,m:2,q:4,s:4,t:2,v:1,z:0};var r=[];if(T(A)&&T(A[0])){r=pathClone(A);}if(!r.length){String(A).replace(P,function(u,t,x){var w=[];var v=i.call(t);x.replace(G,function(z,y){y&&w.push(+y);});if(v=="m"&&w.length>2){r.push([t].concat(w.splice(0,2)));v="l";t=t=="m"?"l":"L";}while(w.length>=s[v]){r.push([t].concat(w.splice(0,s[v])));if(!s[v]){break;}}});}r.toString=B._path2string;return r;});B.findDotsAtSegment=function(r,A,AN,AL,z,v,AB,AA,AH){var AF=1-AH;var AE=k(AF,3)*r+k(AF,2)*3*AH*AN+AF*3*AH*AH*z+k(AH,3)*AB;var AC=k(AF,3)*A+k(AF,2)*3*AH*AL+AF*3*AH*AH*v+k(AH,3)*AA;var AJ=r+2*AH*(AN-r)+AH*AH*(z-2*AN+r);var AI=A+2*AH*(AL-A)+AH*AH*(v-2*AL+A);var AM=AN+2*AH*(z-AN)+AH*AH*(AB-2*z+AN);var AK=AL+2*AH*(v-AL)+AH*AH*(AA-2*v+AL);var AG=(1-AH)*r+AH*AN;var AD=(1-AH)*A+AH*AL;var u=(1-AH)*z+AH*AB;var s=(1-AH)*v+AH*AA;var w=(90-l.atan((AJ-AM)/(AI-AK))*180/l.PI);if(AJ>AM||AI<AK){w+=180;}return{x:AE,y:AC,m:{x:AJ,y:AI},n:{x:AM,y:AK},start:{x:AG,y:AD},end:{x:u,y:s},alpha:w};};B.pathDimensions=O.cached(function(AC){if(!AC){return{x:0,y:0,width:0,height:0};}AC=B.path2curve(AC);var AA=0;var z=0;var t=[];var r=[];var s;for(var v=0,u=AC.length;v<u;v++){s=AC[v];if(s[0]=="M"){AA=s[1];z=s[2];t.push(AA);r.push(z);}else{var w=B.curveDim(AA,z,s[1],s[2],s[3],s[4],s[5],s[6]);t=t.concat(w.min.x,w.max.x);r=r.concat(w.min.y,w.max.y);AA=s[5];z=s[6];
}}var A=L.apply(0,t);var AB=L.apply(0,r);return{x:A,y:AB,width:g.apply(0,t)-A,height:g.apply(0,r)-AB};});B.pathClone=function(v){var s=[];if(!T(v)||!T(v&&v[0])){v=B.parsePathString(v);}for(var r=0,u=v.length;r<u;r++){s[r]=[];for(var A=0,t=v[r].length;A<t;A++){s[r][A]=v[r][A];}}s.toString=B._path2string;return s;};B._pathToRelative=O.cached(function(u){if(!T(u)||!T(u&&u[0])){u=B.parsePathString(u);}var AC=[];var AF=0;var AE=0;var AI=0;var AH=0;var s=0;if(u[0][0]=="M"){AF=u[0][1];AE=u[0][2];AI=AF;AH=AE;s++;AC.push(["M",AF,AE]);}for(var AA=s,t=u.length;AA<t;AA++){var A=AC[AA]=[];var AG=u[AA];if(AG[0]!=i.call(AG[0])){A[0]=i.call(AG[0]);switch(A[0]){case"a":A[1]=AG[1];A[2]=AG[2];A[3]=AG[3];A[4]=AG[4];A[5]=AG[5];A[6]=+(AG[6]-AF).toFixed(3);A[7]=+(AG[7]-AE).toFixed(3);break;case"v":A[1]=+(AG[1]-AE).toFixed(3);break;case"m":AI=AG[1];AH=AG[2];default:for(var w=1,AD=AG.length;w<AD;w++){A[w]=+(AG[w]-((w%2)?AF:AE)).toFixed(3);}}}else{A=AC[AA]=[];if(AG[0]=="m"){AI=AG[1]+AF;AH=AG[2]+AE;}for(var v=0,z=AG.length;v<z;v++){AC[AA][v]=AG[v];}}var AB=AC[AA].length;switch(AC[AA][0]){case"z":AF=AI;AE=AH;break;case"h":AF+=+AC[AA][AB-1];break;case"v":AE+=+AC[AA][AB-1];break;default:AF+=+AC[AA][AB-2];AE+=+AC[AA][AB-1];}}AC.toString=B._path2string;return AC;});B.pathToRelative=function(A){return B.pathClone(B._pathToRelative(A));};B._pathToAbsolute=O.cached(function(u){if(!T(u)||!T(u&&u[0])){u=B.parsePathString(u);}var AB=[];var AE=0;var AD=0;var AH=0;var AG=0;var s=0;if(u[0][0]=="M"){AE=+u[0][1];AD=+u[0][2];AH=AE;AG=AD;s++;AB[0]=["M",AE,AD];}for(var AA=s,t=u.length;AA<t;AA++){var A=AB[AA]=[];var AF=u[AA];if(AF[0]!=W.call(AF[0])){A[0]=W.call(AF[0]);switch(A[0]){case"A":A[1]=AF[1];A[2]=AF[2];A[3]=AF[3];A[4]=AF[4];A[5]=AF[5];A[6]=+(AF[6]+AE);A[7]=+(AF[7]+AD);break;case"V":A[1]=+AF[1]+AD;break;case"H":A[1]=+AF[1]+AE;break;case"M":AH=+AF[1]+AE;AG=+AF[2]+AD;default:for(var w=1,AC=AF.length;w<AC;w++){A[w]=+AF[w]+((w%2)?AE:AD);}}}else{for(var v=0,z=AF.length;v<z;v++){AB[AA][v]=AF[v];}}switch(A[0]){case"Z":AE=AH;AD=AG;break;case"H":AE=A[1];break;case"V":AD=A[1];break;case"M":AH=AB[AA][AB[AA].length-2];AG=AB[AA][AB[AA].length-1];default:AE=AB[AA][AB[AA].length-2];AD=AB[AA][AB[AA].length-1];}}AB.toString=B._path2string;return AB;});B.pathToAbsolute=function(A){return B.pathClone(B._pathToAbsolute(A));};B.lineToCubicCurve=function(r,t,A,s){return[r,t,A,s,A,s];};B.quadraticToCubicCurve=function(r,t,w,u,A,s){var v=1/3;var x=2/3;return[v*r+x*w,v*t+x*u,v*A+x*w,v*s+x*u,A,s];};B.arcToCubicCurve=function(AH,Am,AR,AP,AJ,AC,v,AG,Al,AK){var u=l.PI;var AO=u*120/180;var A=u/180*(+AJ||0);var AU=[];var AS;var Ai=O.cached(function(An,Aq,t){var Ap=An*l.cos(t)-Aq*l.sin(t),Ao=An*l.sin(t)+Aq*l.cos(t);return{x:Ap,y:Ao};});if(!AK){AS=Ai(AH,Am,-A);AH=AS.x;Am=AS.y;AS=Ai(AG,Al,-A);AG=AS.x;Al=AS.y;var r=l.cos(u/180*AJ);var AE=l.sin(u/180*AJ);var AW=(AH-AG)/2;var AV=(Am-Al)/2;var Ag=(AW*AW)/(AR*AR)+(AV*AV)/(AP*AP);if(Ag>1){Ag=l.sqrt(Ag);AR=Ag*AR;AP=Ag*AP;}var s=AR*AR;var AZ=AP*AP;var Ab=(AC==v?-1:1)*l.sqrt(l.abs((s*AZ-s*AV*AV-AZ*AW*AW)/(s*AV*AV+AZ*AW*AW)));var AM=Ab*AR*AV/AP+(AH+AG)/2;var AL=Ab*-AP*AW/AR+(Am+Al)/2;var AB=l.asin(((Am-AL)/AP).toFixed(7));var AA=l.asin(((Al-AL)/AP).toFixed(7));AB=AH<AM?u-AB:AB;AA=AG<AM?u-AA:AA;AB<0&&(AB=u*2+AB);AA<0&&(AA=u*2+AA);if(v&&AB>AA){AB=AB-u*2;}if(!v&&AA>AB){AA=AA-u*2;}}else{AB=AK[0];AA=AK[1];AM=AK[2];AL=AK[3];}var AF=AA-AB;if(l.abs(AF)>AO){var AN=AA;var AQ=AG;var AD=Al;AA=AB+AO*(v&&AA>AB?1:-1);AG=AM+AR*l.cos(AA);Al=AL+AP*l.sin(AA);AU=B.arcToCubicCurve(AG,Al,AR,AP,AJ,0,v,AQ,AD,[AA,AN,AM,AL]);}AF=AA-AB;var z=l.cos(AB);var Ak=l.sin(AB);var w=l.cos(AA);var Aj=l.sin(AA);var AX=l.tan(AF/4);var Aa=4/3*AR*AX;var AY=4/3*AP*AX;var Ah=[AH,Am];var Af=[AH+Aa*Ak,Am-AY*z];var Ae=[AG+Aa*Aj,Al-AY*w];var Ac=[AG,Al];Af[0]=2*Ah[0]-Af[0];Af[1]=2*Ah[1]-Af[1];if(AK){return[Af,Ae,Ac].concat(AU);}else{AU=[Af,Ae,Ac].concat(AU).join().split(",");var AT=[];for(var Ad=0,AI=AU.length;Ad<AI;Ad++){AT[Ad]=Ad%2?Ai(AU[Ad-1],AU[Ad],A).y:Ai(AU[Ad],AU[Ad+1],A).x;}return AT;}};B.findDotAtSegment=function(r,A,u,s,z,y,x,w,AA){var v=1-AA;return{x:k(v,3)*r+k(v,2)*3*AA*u+v*3*AA*AA*z+k(AA,3)*x,y:k(v,3)*A+k(v,2)*3*AA*s+v*3*AA*AA*y+k(AA,3)*w};};B.curveDim=O.cached(function(s,A,u,t,AH,AG,AD,AA){var AF=(AH-2*u+s)-(AD-2*AH+u);var AC=2*(u-s)-2*(AH-u);var z=s-u;var w=(-AC+l.sqrt(AC*AC-4*AF*z))/2/AF;var v=(-AC-l.sqrt(AC*AC-4*AF*z))/2/AF;var AB=[A,AA];var AE=[s,AD];var r;l.abs(w)>1000000000000&&(w=0.5);l.abs(v)>1000000000000&&(v=0.5);if(w>0&&w<1){r=B.findDotAtSegment(s,A,u,t,AH,AG,AD,AA,w);AE.push(r.x);AB.push(r.y);}if(v>0&&v<1){r=B.findDotAtSegment(s,A,u,t,AH,AG,AD,AA,v);AE.push(r.x);AB.push(r.y);}AF=(AG-2*t+A)-(AA-2*AG+t);AC=2*(t-A)-2*(AG-t);z=A-t;w=(-AC+l.sqrt(AC*AC-4*AF*z))/2/AF;v=(-AC-l.sqrt(AC*AC-4*AF*z))/2/AF;l.abs(w)>1000000000000&&(w=0.5);l.abs(v)>1000000000000&&(v=0.5);if(w>0&&w<1){r=B.findDotAtSegment(s,A,u,t,AH,AG,AD,AA,w);AE.push(r.x);AB.push(r.y);}if(v>0&&v<1){r=B.findDotAtSegment(s,A,u,t,AH,AG,AD,AA,v);AE.push(r.x);AB.push(r.y);}return{min:{x:L.apply(0,AE),y:L.apply(0,AB)},max:{x:g.apply(0,AE),y:g.apply(0,AB)}};});B._path2curve=O.cached(function(AE,AA){var t=B.pathToAbsolute(AE);var AB=AA&&B.pathToAbsolute(AA);var AC={x:0,y:0,bx:0,by:0,X:0,Y:0,qx:null,qy:null};var A={x:0,y:0,bx:0,by:0,X:0,Y:0,qx:null,qy:null};var w=function(AG,AH){var AF;var AI;if(!AG){return["C",AH.x,AH.y,AH.x,AH.y,AH.x,AH.y];}if(!(AG[0] in p)){AH.qx=AH.qy=null;}switch(AG[0]){case"M":AH.X=AG[1];AH.Y=AG[2];break;case"A":AG=["C"].concat(B.arcToCubicCurve.apply(0,[AH.x,AH.y].concat(AG.slice(1))));break;case"S":AF=AH.x+(AH.x-(AH.bx||AH.x));AI=AH.y+(AH.y-(AH.by||AH.y));AG=["C",AF,AI].concat(AG.slice(1));break;case"T":AH.qx=AH.x+(AH.x-(AH.qx||AH.x));AH.qy=AH.y+(AH.y-(AH.qy||AH.y));AG=["C"].concat(B.quadraticToCubicCurve(AH.x,AH.y,AH.qx,AH.qy,AG[1],AG[2]));break;case"Q":AH.qx=AG[1];AH.qy=AG[2];AG=["C"].concat(B.quadraticToCubicCurve(AH.x,AH.y,AG[1],AG[2],AG[3],AG[4]));break;case"L":AG=["C"].concat(B.lineToCubicCurve(AH.x,AH.y,AG[1],AG[2]));break;case"H":AG=["C"].concat(B.lineToCubicCurve(AH.x,AH.y,AG[1],AH.y));
break;case"V":AG=["C"].concat(B.lineToCubicCurve(AH.x,AH.y,AH.x,AG[1]));break;case"Z":AG=["C"].concat(B.lineToCubicCurve(AH.x,AH.y,AH.X,AH.Y));break;}return AG;};var r=function(AF,AG){if(AF[AG].length>7){AF[AG].shift();var AH=AF[AG];while(AH.length){AF.splice(AG++,0,["C"].concat(AH.splice(0,6)));}AF.splice(AG,1);v=g(t.length,AB&&AB.length||0);}};var s=function(AJ,AI,AG,AF,AH){if(AJ&&AI&&AJ[AH][0]=="M"&&AI[AH][0]!="M"){AI.splice(AH,0,["M",AF.x,AF.y]);AG.bx=0;AG.by=0;AG.x=AJ[AH][1];AG.y=AJ[AH][2];v=g(t.length,AB&&AB.length||0);}};for(var y=0,v=g(t.length,AB&&AB.length||0);y<v;y++){t[y]=w(t[y],AC);r(t,y);AB&&(AB[y]=w(AB[y],A));AB&&r(AB,y);s(t,AB,AC,A,y);s(AB,t,A,AC,y);var x=t[y];var AD=AB&&AB[y];var u=x.length;var z=AB&&AD.length;AC.x=x[u-2];AC.y=x[u-1];AC.bx=parseFloat(x[u-4])||AC.x;AC.by=parseFloat(x[u-3])||AC.y;A.bx=AB&&(parseFloat(AD[z-4])||A.x);A.by=AB&&(parseFloat(AD[z-3])||A.y);A.x=AB&&AD[z-2];A.y=AB&&AD[z-1];}return AB?[t,AB]:t;});B.path2curve=function(r,A){return B.pathClone(B._path2curve(r,A));};B.parseDots=O.cached(function(z){var y=[];for(var v=0,s=z.length;v<s;v++){var A={};var x=z[v].match(U);A.color=X.getRGB(x[1]);if(A.color.error){return null;}A.color=A.color.hex;x[2]&&(A.offset=x[2]+"%");y.push(A);}for(v=1,s=y.length-1;v<s;v++){if(!y[v].offset){var r=parseFloat(y[v-1].offset||0);var t=0;for(var u=v+1;u<s;u++){if(y[u].offset){t=y[u].offset;break;}}if(!t){t=100;u=s;}t=parseFloat(t);var w=(t-r)/(u-v+1);for(;v<u;v++){r+=w;y[v].offset=r+"%";}}}return y;});B.XYToString=function(){return this.x+E+this.y;};B.getPointAtSegmentLength=O.cached(function(s,A,w,v,AC,AB,AA,z,t){var y=0;var u;for(var x=0;x<1.01;x+=0.01){var r=B.findDotAtSegment(s,A,w,v,AC,AB,AA,z,x);x&&(y+=k(k(u.x-r.x,2)+k(u.y-r.y,2),0.5));if(y>=t){return r;}u=r;}});var a={};B.getPointAtSegmentLength=function(u,r,y,x,AG,AF,AE,AC,v){var AB=0;var AA=100;var t=[u,r,y,x,AG,AF,AE,AC].join();var A=a[t];var w;var s;if(!A){A={data:[]};a[t]=A;}if(A.timer){clearTimeout(A.timer);}A.timer=setTimeout(function(){delete a[t];},2000);if(v!=null){var AD=getPointAtSegmentLength(u,r,y,x,AG,AF,AE,AC);AA=~~AD*10;}for(var z=0;z<AA+1;z++){if(A.data[v]>z){s=A.data[z*AA];}else{s=B.findDotsAtSegment(u,r,y,x,AG,AF,AE,AC,z/AA);A.data[z]=s;}if(z){AB+=k(k(w.x-s.x,2)+k(w.y-s.y,2),0.5);}if(v!=null&&AB>=v){return s;}if(v==null){return AB;}}};B.getLengthFactory=function(A,r){return function(AF,u,v){AF=B.path2curve(AF);var AC;var AB;var s;var w;var t="";var AE={};var AD;var AA=0;for(var z=0,u=AF.length;z<u;z++){s=AF[z];if(s[0]=="M"){AC=+s[1];AB=+s[2];}else{w=B.getPointAtSegmentLength(AC,AB,s[1],s[2],s[3],s[4],s[5],s[6]);if(AA+w>u){if(r&&!AE.start){AD=B.getPointAtSegmentLength(AC,AB,s[1],s[2],s[3],s[4],s[5],s[6],u-AA);t+=["C",AD.start.x,AD.start.y,AD.m.x,AD.m.y,AD.x,AD.y];if(v){return t;}AE.start=t;t=["M",AD.x,AD.y+"C",AD.n.x,AD.n.y,AD.end.x,AD.end.y,s[5],s[6]].join();AA+=w;AC=+s[5];AB=+s[6];continue;}if(!A&&!r){AD=B.getPointAtSegmentLength(AC,AB,s[1],s[2],s[3],s[4],s[5],s[6],u-AA);return{x:AD.x,y:AD.y,alpha:AD.alpha};}}AA+=w;AC=+s[5];AB=+s[6];}t+=s;}AE.end=t;AD=A?AA:r?AE:B.findDotsAtSegment(AC,AB,s[1],s[2],s[3],s[4],s[5],s[6],1);AD.alpha&&(AD={x:AD.x,y:AD.y,alpha:AD.alpha});return AD;};};B.getTotalLength=B.getLengthFactory(1);B.getPointAtLength=B.getLengthFactory();B.getSubpathsAtLength=B.getLengthFactory(0,1);B.applyAttributes=function(s,A,u){var x;if(u!=null){x={};x[A]=u;}else{if(A!=null&&e(A)){x=A;}}var w=s.paper.customAttributes;for(var r in w){if(w.hasOwnProperty(r)&&x.hasOwnProperty(r)&&Q(w[r])){var t=w[r].apply(s,[].concat(x[r]));s.attrs[r]=x[r];for(var v in t){if(t.hasOwnProperty(v)){x[v]=t[v];}}}}return x;};H.prototype={attr:f,blur:f,clone:function(){if(this.removed){return null;}var A=this.attr();delete A.scale;delete A.translation;return this.paper[this.type]().attr(A);},createElement:f,detach:function(s,r){var A=this;O.detach(H._getEventType(s),r,(A.shape||A.node||Z));return A;},getRegion:f,getPointAtLength:function(A){if(this.type!="path"){return;}return B.getPointAtLength(this.attrs.path,A);},getSubpath:function(s,r){if(this.type!="path"){return;}if(l.abs(this.getTotalLength()-r)<"1e-6"){return B.getSubpathsAtLength(this.attrs.path,s).end;}var A=B.getSubpathsAtLength(this.attrs.path,r,1);return s?B.getSubpathsAtLength(A,s).end:A;},getTotalLength:function(){if(this.type!="path"){return;}if(this.node.getTotalLength){return this.node.getTotalLength();}return B.getTotalLength(this.attrs.path);},hide:f,hover:function(r,A){return this.on("mouseover",r).on("mouseout",A);},insertAfter:f,insertBefore:f,on:function(u,t,r){var A=this;var s=H._getEventType(u);O.on(s,O.bind(t,r||A),A.shape||A.node||Z,H._getEventConfig(s,u));return A;},remove:f,resetScale:function(){if(this.removed){return this;}this._.sx=1;this._.sy=1;this.attrs.scale="1 1";},rotate:f,scale:function(AR,AQ,AJ,AH){if(this.removed){return this;}if(AR==null&&AQ==null){return{x:this._.sx,y:this._.sy,toString:B.XYToString};}AQ=AQ||AR;!+AQ&&(AQ=AR);var r;var A;var AZ;var AY;var Ac=this.attrs;if(AR!=0){var Ad=this.getRegion();var v=Ad.x+Ad.width/2;var u=Ad.y+Ad.height/2;var AW=l.abs(AR/this._.sx);var AV=l.abs(AQ/this._.sy);AJ=(+AJ||AJ==0)?AJ:v;AH=(+AH||AH==0)?AH:u;var AB=~~(this._.sx>0);var AA=~~(this._.sy>0);var AI=~~(AR/l.abs(AR));var AG=~~(AQ/l.abs(AQ));var z=AW*AI;var w=AV*AG;var AS=this.node.style;var AP=AJ+l.abs(v-AJ)*z*(v>AJ==AB?1:-1);var AO=AH+l.abs(u-AH)*w*(u>AH==AA?1:-1);var AM=(AR*AI>AQ*AG?AV:AW);switch(this.type){case"rect":case"image":var AD=Ac.width*AW;var AN=Ac.height*AV;this.attr({height:AN,r:Ac.r*AM,width:AD,x:AP-AD/2,y:AO-AN/2});break;case"circle":case"ellipse":this.attr({rx:Ac.rx*AI*AW,ry:Ac.ry*AG*AV,r:Ac.r*L(AI*AW,AG*AV),cx:AP,cy:AO});break;case"text":this.attr({x:AP,y:AO});break;case"path":var AU=B.pathToRelative(Ac.path);var AC=true;var AL=AB?z:AW;var AK=AA?w:AV;for(var Ab=0,AE=AU.length;Ab<AE;Ab++){var AX=AU[Ab];var AF=W.call(AX[0]);if(AF=="M"&&AC){continue;}else{AC=false;}if(AF=="A"){AX[AU[Ab].length-2]*=AL;AX[AU[Ab].length-1]*=AK;AX[1]*=AW;AX[2]*=AV;AX[5]=+(AI+AG?!!+AX[5]:!+AX[5]);
}else{if(AF=="H"){for(var Aa=1,t=AX.length;Aa<t;Aa++){AX[Aa]*=AL;}}else{if(AF=="V"){for(Aa=1,t=AX.length;Aa<t;Aa++){AX[Aa]*=AK;}}else{for(Aa=1,t=AX.length;Aa<t;Aa++){AX[Aa]*=(Aa%2)?AL:AK;}}}}}var AT=B.pathDimensions(AU);r=AP-AT.x-AT.width/2;A=AO-AT.y-AT.height/2;AU[0][1]+=r;AU[0][2]+=A;this.attr({path:AU});break;}if(this.type in b&&(AI!=1||AG!=1)){if(this.transformations){this.transformations[2]="scale(".concat(AI,",",AG,")");this.node.setAttribute("transform",this.transformations.join(E));r=(AI==-1)?-Ac.x-(AD||0):Ac.x;A=(AG==-1)?-Ac.y-(AN||0):Ac.y;this.attr({x:r,y:A});Ac.fx=AI-1;Ac.fy=AG-1;}else{this.node.filterMatrix=m+".Matrix(M11=".concat(AI,", M12=0, M21=0, M22=",AG,', Dx=0, Dy=0, sizingmethod="auto expand", filtertype="bilinear")');AS.filter=(this.node.filterMatrix||M)+(this.node.filterOpacity||M);}}else{if(this.transformations){this.transformations[2]=M;this.node.setAttribute("transform",this.transformations.join(E));Ac.fx=0;Ac.fy=0;}else{this.node.filterMatrix=M;AS.filter=(this.node.filterMatrix||M)+(this.node.filterOpacity||M);}}Ac.scale=[AR,AQ,AJ,AH].join(E);this._.sx=AR;this._.sy=AQ;}return this;},show:f,toBack:f,toFront:f,toString:function(){return'"'+W.call(this.type)+'": AlloyUI Drawing Element';},translate:f,unhover:function(r,A){return this.detach("mouseover",r).detach("mouseout",A);}};H._getEventConfig=f;H._getEventType=function(A){return A;};D.Element=H;var F=O.Component.create({NAME:"set",EXTENDS:O.ArrayList,prototype:{pop:function(){var A=this;return A._items.pop();},push:function(){var A=this;var r=A._items;var s=arguments;var v=s.length;var u;for(var t=0;t<v;t++){u=s[t];if(u&&u instanceof H||u instanceof F){r[r.length]=s[t];}}return A;},clone:function(){var A=this;var r=new F();r._items=A._items;return r;},invoke:function(s){var A=this;var t=Array.prototype.slice.call(arguments,1);var w;var r=A._items;for(var u=0,v=r.length;u<v;u++){w=r[u];if(s in w){w[s].apply(r[u],t);}}return A;}}});D.Set=F;F.addMethod=function(A){return O.ArrayList.addMethod(D.Set.prototype,A);};var h=O.Object.keys(H.prototype);F.addMethod(h);F.prototype.getRegion=F.prototype.getBBox=function(){var r=this;var A=[];var AA=[];var s=[];var v=[];var t=r._items;for(var u=t.length;u--;){var z=t[u].getRegion();A.push(z.x);AA.push(z.y);s.push(z.x+z.width);v.push(z.y+z.height);}A=L.apply(0,A);AA=L.apply(0,AA);return{x:A,y:AA,width:g.apply(0,s)-A,height:g.apply(0,v)-AA};};F.prototype.attr=function(s,v){var A=this;var r=A._items;if(s&&T(s)&&e(s[0])){for(var t=0,u=s.length;t<u;t++){r[t].attr(s[t]);}}else{for(var t=0,u=r.length;t<u;t++){r[t].attr(s,v);}}return A;};F.prototype.toString=function(){return this._items.join(",");};D.REGEX_ISURL=c;D.REGEX_SEPARATOR=n;D.REGEX_SEPARATOR_GRADIENT=K;D.REGEX_RADIAL_GRADIENT=j;D.MAP_ATTRS_AVAILABLE=N;D.MAP_TYPES_IMAGE_TEXT=b;D.MAP_TYPES_CIRCLE_ELLIPSE=S;D.STR_EMPTY=M;D.STR_FILL=d;D.STR_SPACE=E;D.STR_MS_PROG_ID_PREFIX=m;D.Util=B;D.Element=H;O.Drawing=D;},"1.0.1",{requires:["aui-base","aui-color-util","substitute"]});