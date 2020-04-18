function init() {}

$('#projectModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    $('#projectModalSubmit').unbind();

    let projectId = $(event.relatedTarget).data('project-id');
    let link = projectId
        // If projectId is valid, then we are in edit mode
        ? CONTEXT_PATH + '/project/edit/' + projectId
        // Otherwise we are in creation mode
        : CONTEXT_PATH + '/project/add';

    // Update modal's title depending on mode
    $('#projectModalLabel').html(projectId ? 'Edit Project <span class="small text-gray-500">#' + projectId + '</span>' : 'Add New Project');

    $.get(link, function(data) {
        $('#projectModalBody').html(data);
        $('#projectModalSubmit').click(function(event) {
            // Submit
            $.post(link, $('#projectModalForm').serialize(), function () {
                window.location.reload();
            });
        });
    });
});

$('#projectDeleteModal').on('show.bs.modal', function(event) {
    // Unbind handlers to avoid situations in which this button has more than one onclick handler
    const submitButton = $('#projectDeleteModalSubmit');
    submitButton.unbind();

    let projectId = $(event.relatedTarget).data('project-id');

    $('#projectDeleteModal-id').text(projectId);

    $.get(CONTEXT_PATH + '/project/numberoftransactions/' + projectId, function(data) {
        $('#projectDeleteModalBody').children().hide();

        let deletePossible = true;

        const transCnt = parseInt(data);
        if (data && transCnt === 1) {
            $('#projectDeleteModal-assignedTransactions').show();
            $('#projectDeleteModal-assignedCount').text(transCnt);
            deletePossible = false;
        }

        if ($('#list').children('tr').length === 1) {
            $('#projectDeleteModal-lastProject').show();
            deletePossible = false;
        }

        if (deletePossible) {
            $('#projectDeleteModal-possible').show();
            submitButton.show();
            submitButton.click(function(event) {
                $.get(CONTEXT_PATH + '/project/delete/' + projectId, function () {
                    window.location.reload();
                });
            });
        }
        else
            submitButton.hide();
    });
});

$('.transaction-count').each(function () {
    const target = $(this);
    const projectId = $(this).data('project-id');
    $.get(CONTEXT_PATH + '/project/numberoftransactions/' + projectId, function (data) {
        target.text(data);
    });
});

$('.import-count').each(function () {
    const target = $(this);
    const projectId = $(this).data('project-id');
    $.get(CONTEXT_PATH + '/project/numberofimports/' + projectId, function (data) {
        target.text(data);
    });
});

$('#archived-projects-toggle').click(function () {
    $('#archived-projects').slideToggle();
});

$('.archive-project').click(function () {
    const projectId = $(this).data('project-id');
    if (projectId) {
        $.get(CONTEXT_PATH + '/project/archive/' + projectId, function (data) {
            console.log(data);
            location.reload();
        });
    }
});

$('.restore-project').click(function () {
    const projectId = $(this).data('project-id');
    if (projectId) {
        $.get(CONTEXT_PATH + '/project/restore/' + projectId, function () {
            location.reload();
        });
    }
});

$('.activate-project').click(function () {
    const projectId = $(this).data('project-id');
    if (projectId) {
        $.get(CONTEXT_PATH + '/user/setcurrentproject/' + projectId, function () {
            location.reload();
        });
    }
});
