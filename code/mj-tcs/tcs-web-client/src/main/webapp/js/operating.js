$(function(){




	$(".elements-blocks-title li").each(function(){

        $(this).click(function(){
            $(".elements-blocks-title li").removeClass("active");
            $(this).addClass("active");
            // alert($(this).index());
            if($(this).index() == 0){
            	$(".elements-blocks .elements-content").css("display","block");
            	$(".elements-blocks .blocks-content").css("display","none");

            } else if($(this).index() == 1) {
            	
				$(".elements-blocks .elements-content").css("display","none");
            	$(".elements-blocks .blocks-content").css("display","block");
            } else {

            }
        })
     
    });

    $(".operation-list .to").click(function() {
    	if ($(".top-panel .top-panel-to").css("display") == 'none') {
    		$(".top-panel .top-panel-to").css("display","block");
    	} else {
    		$(".top-panel .top-panel-to").css("display","none");
    	}
    	
    })


})