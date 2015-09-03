var url = '';

jQuery(function(){
	var kind = jQuery("#kind").val()
	showTeachingHelpWorkload(kind)
	
	var protocol = window.location.href.split('/')[0]
	var site = window.location.href.split('/')[2]
	url = protocol + '//' + site
	
	jQuery.getJSON(url+"/request/list/by/kind/"+kind, function(activities) {
		addActivities(activities)
	})
})
jQuery("#kind").change(function(){
	var kind = jQuery(this).val()
	showTeachingHelpWorkload(kind)
	
	jQuery.getJSON(url+"/request/list/by/kind/"+kind, function(activities) {
		addActivities(activities)
	})
})

jQuery("#activity").change(function(){
	validateWorkload()
})

function addActivities(activities){
	jQuery("#activity").empty()
	jQuery.each(activities, function(index,activity){
		jQuery("#activity").append("<option value='"+activity.value+"' data-workload='"+activity.maxWorkload+"'>"+i18n(activity.value)+"</option>")
	})
	validateWorkload()
}

var validDocument = false
var validEvent = false
var validDescription = false
var validParticipation = false
var validInstitution = false
var validPeriod = false
var validWorkload = false

function showTeachingHelpWorkload(kind){
	if(isTeaching(kind)){
		jQuery("#teachingHelpWorkload").removeClass('hide')
	}else{
		jQuery("#teachingHelpWorkload").addClass('hide')
	}
}
function isTeaching(kind){
	return kind === "teaching"
}

function isSemester(period){
	var semesterRegex = /[0-9][0-9][0-9][0-9][.][1-2]/;
	return semesterRegex.exec(period) != null
}

function validateForm(){
	if(validDocument && validEvent && validDescription && validParticipation && validInstitution && validPeriod && validWorkload){
		jQuery("#submit").removeAttr('disabled')
	}else{
		jQuery("#submit").attr('disabled','disabled')
	}
}


function showAlert(field){
	jQuery("#"+field+"Div").addClass("has-error").addClass("has-feedback")
	jQuery("#x"+field).removeClass("hide").addClass("show")
	jQuery("#"+field+"ErrorMessage").removeClass("hide").addClass("show")
}

function removeAlert(field){
	jQuery("#"+field+"Div").removeClass("has-error").removeClass("has-feedback")
	jQuery("#x"+field).removeClass("show").addClass("hide")
	jQuery("#"+field+"ErrorMessage").removeClass("show").addClass("hide")
}

jQuery("#document").change(function(){
	var value = jQuery("#document").val()
	console.log(value)
	if(value == ""){
		validDocument = false
		showAlert("document")
	}else{
		validDocument = true
		removeAlert("document")
	}
	validateForm()
})

jQuery("#event").focusout(function(){
	var value = jQuery("#event").val()
	if(value == ""){
		validEvent = false
		showAlert("event")
	}else{
		validEvent = true
		removeAlert("event")
	}
	validateForm()
})
jQuery("#description").focusout(function(){
	var value = jQuery("#description").val()
	if(value == ""){
		validDescription = false
		showAlert("description")
	}else{
		validDescription = true
		removeAlert("description")
	}
	validateForm()
})
jQuery("#participation").focusout(function(){
	var value = jQuery("#participation").val()
	if(value == ""){
		validParticipation = false
		showAlert("participation")
	}else{
		validParticipation = true
		removeAlert("participation")
	}
	validateForm()
})
jQuery("#institution").focusout(function(){
	var value = jQuery("#institution").val()
	if(value == ""){
		validInstitution = false
		showAlert("institution")
	}else{
		validInstitution = true
		removeAlert("institution")
	}
	validateForm()
})
jQuery("#period").focusout(function(){
	var value = jQuery("#period").val()
	if(value == ""){
		validPeriod = false
		showAlert("period")
	}else{
		if(isSemester(value)){
			validPeriod = true
			removeAlert("period")
		}else{
			validPeriod = false
			showAlert("period")
		}
	}
	validateForm()
})

function validateWorkload(){
	var workload = parseInt(jQuery("#workload").val())
	var maxWorkload = parseInt(jQuery("#activity").find(':selected').data('workload'))
	
	if(isNaN(workload) || workload > maxWorkload){
		jQuery("#workloadDiv").addClass("has-error").addClass("has-feedback")
		jQuery("#xworkload").removeClass("hide").addClass("show")
		jQuery("#errorMessageWorkload").removeClass("hide").addClass("show")
		jQuery("#maxWorkload").html(maxWorkload)
		validWorkload = false
	}else{
		jQuery("#workloadDiv").removeClass("has-error").removeClass("has-feedback")
		jQuery("#xworkload").removeClass("show").addClass("hide")
		jQuery("#errorMessageWorkload").removeClass("show").addClass("hide")
		jQuery("#maxWorkload").html("")
		validWorkload = true
	}
	validateForm()
}

jQuery("#workload").focusout(function(){
	validateWorkload()
})

function i18n(value){
	switch(value) {
		case "foreign_language_course": return "Cursos de língua estrangeira"
		case "computer_course": return "Curso de informática"
		case "complement_course": return "Cursos de complementação de conteúdos das disciplinas do curso"
		case "science_research": return "Iniciação científica"
		case "research_project": return "Pesquisa em projetos do curso"
		case "study_group": return "Participação em grupo de estudo"
		case "univerty_week_presentation": return "Apresentação de trabalhos na Semana Universitária"
		case "congress_presentation": return "Apresentação de trabalhos em congressos, simpósios, encontros nacionais"
		case "academic_award": return "Prêmio acadêmico, artístico ou cultural"
		case "publication": return "Trabalhos completos publicados em anais"
		case "publication_isbn": return "Publicação de livros de divulgação científica com ISBN"
		case "publication_isbn_chapter": return "Publicação de capítulo de livros com ISBN"
		case "publication_book": return "Publicação de livros na área de conhecimento do Curso"
		case "publication_abstract_local": return "Publicação de Resumos em Congressos Científicos locais"
		case "publication_abstract_region": return "Publicação de Resumos em Congressos Científicos regionais"
		case "publication_abstract_nation": return "Publicação de Resumos em Congressos Científicos nacionais"
		case "publication_abstract_international": return "Publicação de Resumos em Congressos Científicos internacionais"
		case "publication_magazine_local": return "Publicação de Artigos em revistas locais com corpo editorial"
		case "publication_magazine_nation": return "Publicação de Artigos em revistas nacionais com corpo editorial"
		case "publication_magazine_international": return "Publicação de Artigos em revistas internacionais com corpo editorial" 
		case "publication_magazine_specific": return "Publicação de Artigos de divulgação científica, tecnológica e artística em revista especializada"
		case "publication_paper": return "Publicação de Artigos de divulgação científica, tecnológica e artística em jornais"
		case "pet": return "Participação em Programa de Educação Tutorial"
		case "tutoring": return "Participação em Programas de Monitoria Acadêmica"
		case "event_participation": return "Participação em eventos"
		case "lab_internship": return "Estágios em laboratórios de ensino e de pesquisa"
		case "internship": return "Estágios"
		case "event_planning": return "Participação em comissões organizadoras de eventos acadêmicos, artísticos e culturais"
		case "class_planning": return "Produção de material didático com orientação de Professores"
		case "school_participation": return "Participação como representante estudantil nos Colegiados"
		case "community_participation": return "Participação em Projetos ou Programas registrados na Pró-Reitoria de Extensão"
	}
	console.log(value)
}