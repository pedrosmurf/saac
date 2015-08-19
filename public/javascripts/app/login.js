jQuery("#loginForm").submit(function(event) {
	jQuery("#hashPassword").val(jQuery.md5(jQuery("#password").val()))
});