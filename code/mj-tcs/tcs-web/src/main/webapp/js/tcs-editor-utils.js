"use strict";

var editorUtils = function(){};

// STATIC METHODS
editorUtils.extend = function (){
    for(var i=1; i< arguments.length; i++)
        for(var key in arguments[i])
            if(arguments[i].hasOwnProperty(key))
                arguments[0][key] = arguments[i][key];
    return arguments[0];
};

editorUtils.pixelize = function (val){
    return val + 'px';
}

editorUtils.prependChild = function (container, element){
    return container.insertBefore(element,container.firstChild);
};

editorUtils.addClasss = function (element, classNames){
    if(!(classNames instanceof Array))
    {
        classNames = [classNames];
    }

    classNames.forEach(function (name){
        element.className += ' ' + name;
    });

    return element;

};

editorUtils.removeClasss = function (element, classNames){
    var curCalsss = element.className;
    if(!(classNames instanceof Array))
    {
        classNames = [classNames];
    }

    classNames.forEach(function (name){
        curCalsss = curCalsss.replace(name, '');
    });
    element.className = curCalsss;
    return element;

};

editorUtils.offsetLeft = function (element) {
    var left = element.offsetLeft;
    var parent = element.offsetParent;
    while (parent != null) {
        left += parent.offsetLeft;
        parent = parent.offsetParent;
    }
    return left;
};

editorUtils.offsetTop = function (element) {
    var top = element.offsetTop;
    var parent = element.offsetParent;
    while (parent != null) {
        top += parent.offsetTop;
        parent = parent.offsetParent;
    }
    return top;
}