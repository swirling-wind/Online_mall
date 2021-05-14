$(function(){
    dropDown("user-center");
    dropDown("e-mall");
});

function dropDown(id){
    var oId = document.getElementById(id);
    var oDdd = oId.getElementsByTagName("div")[0];
    oId.onmouseenter = function(){
        oId.className = "libg";
        oDdd.style.display = "block";
        oDdd.style.zIndex = "99";
    }
    oId.onmouseleave = function(){
        oId.className = " ";
        oDdd.style.display = "none";
        oDdd.style.zIndex = "0";
    }
}