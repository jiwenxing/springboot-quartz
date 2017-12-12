$(function() {

	//run job once
    $(".btnRun").click(function() {
    	var jobId = $(this).parent().data("id");
        $.ajax({
            url: "/api/runJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+jobId).text(),
                "jobGroup": $("#group_"+jobId).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("run success!");  
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
    
    //pause job
    $(".btnPause").click(function() {
    	var jobId = $(this).parent().data("id");
        $.ajax({
            url: "/api/pauseJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+jobId).text(),
                "jobGroup": $("#group_"+jobId).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("pause success!");
                	location.reload();
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
    
    //resume job
    $(".btnResume").click(function() {
    	var jobId = $(this).parent().data("id");
        $.ajax({
            url: "/api/resumeJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+jobId).text(),
                "jobGroup": $("#group_"+jobId).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("resume success!");
                	location.reload();
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
    
    //delete job
    $(".btnDelete").click(function() {
    	var jobId = $(this).parent().data("id");
        $.ajax({
            url: "/api/deleteJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+jobId).text(),
                "jobGroup": $("#group_"+jobId).text()
            },
            success: function(res) {
                if (res.valid) {
                	alert("delete success!");
                	location.reload();
                } else {
                	alert(res.msg); 
                }
            }
        });
    });
	
	// update cron expression
    $(".btnEdit").click(
    		function() {
    			$("#myModalLabel").html("cron edit");
    			var jobId = $(this).parent().data("id");
    			$("#jobId").val(jobId);
    			$("#edit_name").val($("#name_"+jobId).text());
    			$("#edit_group").val($("#group_"+jobId).text());
    			$("#edit_cron").val($("#cron_"+jobId).text());
    			$("#edit_status").val($("#status_"+jobId).text());
    			$("#edit_desc").val($("#desc_"+jobId).text());
    			
    			$('#edit_name').attr("readonly","readonly"); 
    			$('#edit_group').attr("readonly","readonly");
    			$('#edit_desc').attr("readonly","readonly");
    			
    			$("#myModal").modal("show");
    });
    
    $("#save").click(
	    function() {
	    	$.ajax({
	            url: "/api/saveOrUpdate?t=" + new Date().getTime(),
	            type: "POST",
	            data:  $('#mainForm').serialize(),
	            success: function(res) {
	            	if (res.valid) {
	                	alert("success!");
	                	location.reload();
	                } else {
	                	alert(res.msg); 
	                }
	            }
	        });
    });


    // create job
    $("#createBtn").click(
    		function() {
    			$("#myModalLabel").html("create job");
    			
    			$("#jobId").val("");
    			$("#edit_name").val("");
    			$("#edit_group").val("");
    			$("#edit_cron").val("");
    			$("#edit_status").val("NORMAL");
    			$("#edit_desc").val("");
    			
    			$('#edit_name').removeAttr("readonly");
    			$('#edit_group').removeAttr("readonly");
    			$('#edit_desc').removeAttr("readonly");
    			
    			$("#myModal").modal("show");
    });
    
    
});