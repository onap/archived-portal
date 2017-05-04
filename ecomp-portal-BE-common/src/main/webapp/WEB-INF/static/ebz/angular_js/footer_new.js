var feedbackText, pageAddress;
(function ( $ ) {
	$(document).on('click', '.feedbackButtonDiv', function (e) {
		$(".feedbackSection").slideToggle(600, function(){
			if($(".feedbackButton").hasClass("closeFeedbackButton")) {
				$(".footerContainer .feedbackButton").html("Feedback<span class='icon-chevron-down'></span>");
				$(".footerContainer .feedbackButton").removeClass("closeFeedbackButton");
			} else {
				$(".footerContainer .feedbackButton").html("Close<span class='icon-chevron-up'></span>");
				$(".footerContainer .feedbackButton").addClass("closeFeedbackButton");
				$(".footerContainer  .feedbackText").focus();
			}

		});
	});

	$(document).on('click', '.closeFeedback', function (e) {
		$(".feedbackSection").slideUp(600, function(){

		});
	});

	$(document).on('click keypress keyup blur paste','textarea[maxlength]', function(e) {
		var maxlength = $(this).attr('maxlength');
		var val = $(this).val();

		if (val.length > maxlength) {
			$(this).val(val.slice(0, maxlength));
		}
	});

	$(document).on('click', '.feedbackSubmitButton', function (e) {	
		var feedbackTextTemp = $(".feedbackText").val();
		feedbackText = $.trim(feedbackTextTemp);
		pageAddress = window.location.href;
		var feedbackData = {
				commentText: feedbackText,
				sourcePage: pageAddress
		};
		if(feedbackText != null && feedbackText != ''){
			$.ajax({
				type: "post",
				dataType: 'json',
				data: feedbackData,
				url: window.dashboardContext + "/mnm/map/common/saveUserComments.jsp",
				success: function (data) {
					$(".feedbackSection").slideUp(600, function(){
						$(".feedbackText").val("");
						$(".footerContainer .feedbackButton").html("Feedback<span class='icon-chevron-down'></span>");
						$(".footerContainer .feedbackButton").removeClass("closeFeedbackButton");
						$(".feedbackButtonDiv").slideUp(600);
						$(".feedbackResultMsg, #feedbackResultDivider").slideDown(600);
						setTimeout(function() {
							$(".feedbackResultMsg, #feedbackResultDivider").slideUp(600);
							$(".feedbackButtonDiv").slideDown(600);
						}, 3000);
					});
				},
				error: function () {
					console.log("Error Saving Feedback.");
				}
			});
		} else {
			e.preventDefault();
			console.log("Invalid Input String")
		}

	});
}( jQuery ));	

$(function() {
	var footerHeight = 0,
	footerTop = 0,
	$footer = $("#footer");

	positionFooter();

	function positionFooter() {

		footerHeight = $footer.height();
		footerTop = ($(window).scrollTop() + $(window).height() - footerHeight) + "px";

		if (($(document.body).height() + footerHeight) < $(window).height()) {
			$footer.css({
				position: "absolute",
				left: "0",
				right: "0"
			}).animate({
				top: footerTop
			}, 0)
		} else {
			$footer.css({
				position: "static"
			})
		}

	}

	$(window)
	.scroll(positionFooter)
	.resize(positionFooter)
});