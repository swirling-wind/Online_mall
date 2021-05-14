window.onload = function(){
	lunbo();//轮播图程序
	categoryInit("sort-list");
}

function categoryInit(domId){
	var oDh = document.getElementById(domId);
	var aDdd = oDh.children;
	for (var i=1;i<aDdd.length;i++) {
		aDdd[i].ind = i;
		aDdd[i].onmouseenter = function(){
			var index = this.ind;
			oDiv = aDdd[index].children[1];
			oDiv.style.visibility = "visible";
		}
		aDdd[i].onmouseleave = function(){
			var index = this.ind;
			oDiv = aDdd[index].children[1];
			oDiv.style.visibility = "hidden";
		}
	}
}

var t = null;//定时器命名
var h = null;//定时器命名
var n = 0;//标识位置用
var s = 0;
var q = 0;

function lunbo(){
	var lbid = document.getElementById("index-banner");
	var lbk = lbid.children;//0为轮播图片部分 1为下标部分
	var albdiv = lbk[0].children;//轮播样式设置部分
	var num = albdiv.length;
	s = n;//计算当前正在显示的图片下标
	n = (n+1)%num;//计算当前应该操作的图片下标
	//一秒内操作
	for (var i = 0;i<1000;i++) {
		var op = albdiv[s].style.opacity;
		var op2 = albdiv[n].style.opacity;
		albdiv[s].style.opacity = (Number(op)*1000-1)/1000;
		albdiv[n].style.opacity = (Number(op2)*1000+1)/1000;
	}
	albdiv[n].style.display = "block";
	albdiv[n].style.zIndex = 2;
	albdiv[s].style.display = "none";
	//一秒后操作
	t && clearInterval(t);
	t = setInterval(function(){
		albdiv[s].style.display = "none";
		albdiv[s].style.opacity = 0;
		albdiv[s].style.zIndex = 0;
		albdiv[n].style.zIndex = 1;
		t && clearInterval(t);    
        t = null;
	},1000);
    h && clearTimeout(h);    
    h=setTimeout(lunbo,2000);
}