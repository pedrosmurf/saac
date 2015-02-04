jQuery("#workload").focusout(function(){
	var workload = parseInt(jQuery("#workload").val())
	var maxWorkload = parseInt(jQuery("#activity").find(':selected').data('workload'))
	
	if(isNaN(workload) || workload > maxWorkload){
		jQuery("#workloadDiv").addClass("has-error").addClass("has-feedback")
		jQuery("#xworkload").removeClass("hide").addClass("show")
		jQuery("#errorMessageWorkload").removeClass("hide").addClass("show")
		jQuery("#maxWorkload").html(maxWorkload)
		jQuery("#submit").attr('disabled','disabled')
		
	}else{
		jQuery("#workloadDiv").removeClass("has-error").removeClass("has-feedback")
		jQuery("#xworkload").removeClass("show").addClass("hide")
		jQuery("#errorMessageWorkload").removeClass("show").addClass("hide")
		jQuery("#maxWorkload").html("")
		jQuery("#submit").removeAttr('disabled')
	}
})