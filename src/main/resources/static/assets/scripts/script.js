$(document).ready(function(){

    // 체크박스 전체 선택
    $(".checkbox_group").on("click", "#check_all", function () {
        $(this).parents(".checkbox_group").find('input').prop("checked", $(this).is(":checked"));
    });

    // 체크박스 개별 선택
    $(".checkbox_group").on("click", ".normal", function() {
        var is_checked = true;

        $(".checkbox_group .normal").each(function(){
            is_checked = is_checked && $(this).is(":checked");
        });

        $("#check_all").prop("checked", is_checked);
    });    

    // 햄버거버튼
    $('.ham').click(function(){
        if ($('.gnb').hasClass('on')) {
            $('.gnb').removeClass('on');
            $('header').removeClass('on');
        }else{
            $('.gnb').addClass('on');
            $('header').addClass('on');
        }
    });

    // 탑버튼
    $('#etc').hide();
    $(window).scroll(function() {

        if ($(this).scrollTop() > 2000) {
            $('#etc').fadeIn();
        } else {
            $('#etc').fadeOut();
        }
    });
    $("#top_btn").click(function() {
        $('html, body').animate({
            scrollTop : 0
        }, 400);
        return false;
    });

    // 모달창 켜기1
    $('.modal_step_open').click(function(){
        var modal_step = $(this).attr('modal_step');

        $('.modal_step').addClass('hidden'); // 이전 모달창을 숨김
        
        $(this).addClass('current');
        $("."+ "modal_step" +modal_step).removeClass('hidden');
    })

    // 모달창 켜기2
    $('.modal_open').on("click", function() {
        $(this).next().removeClass("hidden");
    });

    // 모달창 끄기
    $('.cancerButton').on("click", function() {
        if ($(this).closest(".modal")) {
            $(this).closest(".modal").addClass("hidden");
        }
        else {
            $(this).closest(".modal").addClass("hidden");
        }
    });

    // 모든 모달창 끄기
    $('.allModalClose').on("click", function() {
        $('.modal').addClass("hidden");
    });

    // 첫번째 모달창 끄기
    $('.firstModalClose2').on("click", function() {
        $(this).closest('.modal').addClass("hidden");
        $(this).closest('.modal').next('.modal').removeClass("hidden");
    });   
});
