/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    if ($("div.box-title").text() == "Входящие") {
        $("#in").addClass("active");
        $("ch").removeClass("active");
        $("t").removeClass("active");
        $("s").removeClass("active");
        $("ot").removeClass("active");
    }
    if ($("div.box-title").text() == "Черновики") {
        $("#ch").addClass("active");
        $("in").removeClass("active");
        $("t").removeClass("active");
        $("s").removeClass("active");
        $("ot").removeClass("active");
    }
    if ($("div.box-title").text() == "Коризина") {
        $("#t").addClass("active");
        $("ch").removeClass("active");
        $("in").removeClass("active");
        $("s").removeClass("active");
        $("ot").removeClass("active");
    }
    if ($("div.box-title").text() == "Отправленные") {
        $("#ot").addClass("active");
        $("ch").removeClass("active");
        $("t").removeClass("active");
        $("s").removeClass("active");
        $("in").removeClass("active");
    }
    if ($("div.box-title").text() == "Спам") {
        $("#s").addClass("active");
        $("ch").removeClass("active");
        $("t").removeClass("active");
        $("in").removeClass("active");
        $("ot").removeClass("active");
    }
});
