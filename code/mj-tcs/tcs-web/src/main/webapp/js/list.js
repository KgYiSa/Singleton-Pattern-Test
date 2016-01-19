/**
 * Created by lxh on 15/12/31.
 */


/**
 * Created by lxh on 16/1/12.
 */
var jsonObject = JSON.parse(localStorage.getItem("jsonObject"));
var jsoncosts = JSON.parse(localStorage.getItem("costs"));
var jsonpoint = JSON.parse(localStorage.getItem("point"));
var jsonroute = JSON.parse(localStorage.getItem("route"));
var jsonform = JSON.parse(localStorage.getItem("form"));
$(document).ready(function () {
    //$(".box2").css("display","none");

    var data = eval(jsonObject.stations);
    var table = $(".table");
    $.each(data, function (index, item) {
        var tr = $("<tr></tr>");
        $(tr).click(function () {
            $(this).css("background", "#3A92FF").siblings().css("background", "#ffffff");
            //$(".box2").css("display", "block");
            form($(this).index() - 1);
            route($(this).index() - 1);
            costs($(this).index() - 1);
            point($(this).index() - 1);
            $(".right").hide();
            $(".record-tr").css("background","#fff");
        });
        $.each(item, function (name, val) {
            var td = $("<td>" + val + "</td>");
            tr.append(td);
        });
        table.append(tr);
    });


    var formdata = eval(jsonform.form);
    function form(i) {
        var array = [formdata[i]];
        $.each(array, function (index, item) {
            var j = 0;
            $.each(item, function (index, value) {
                $("input").eq(j).val(value);
                j++;
            });
        });
    }


    var tr = $(".record-tr");
    $(tr).click(function () {
        $(this).css("background", "#3A92FF");
        $(".right").show();
        $(trr).css("background","#000")
    });
    var routetable = $(".table1");
    var Routedata = jsonroute.Route;
    function route(i) {
        var array = [Routedata[i]];
        $.each(array, function (index, item) {
            var a = 0;
            $.each(item, function (index, value) {
                var td = $(".record-td").eq(a).text(value);
                tr.append(td);
                routetable.append(tr);
            });
        });
    }


    var trr;
    var point_table = $(".table2");
    var Point_data = jsonpoint.point;
    function point(i) {
        point_table.empty();
        var array = [];
        for (var j = 0; j < Point_data[i].length; j++) {
            array.push(Point_data[i][j]);
        }
        $.each(array, function (index, item) {
            var tdd = "";
            $.each(item, function (index, value) {
                tdd = tdd + "<td>" + value + "</td>";
            });
            trr = "<tr>" + tdd + "</tr>";
            point_table.append(trr);
        });
    }


    $(".right").css("display", "none");
    var input = $(".input");
    var Costs = jsoncosts.Costs;
    function costs(i) {
        var array = [Costs[i]];
        $.each(array, function (index, item) {
            var a = 0;
            $.each(item, function (index, value) {
                input.eq(a).val(value);
                a++;
            })
        })
    }
});

//第一个按钮功能：原始订单类型。
//第二个按钮功能：可被调度的订单。
//第三个按钮功能：正在执行的订单。
//第四个按钮功能：已经完成的订单。
//第五个按钮功能：出错的订单。
//第六个按钮功能：撤销订单

