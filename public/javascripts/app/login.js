jQuery("#loginForm").submit(function(event) {
	jQuery("#password").val(jQuery.md5(jQuery("#password").val()))
});