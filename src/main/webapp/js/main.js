$('.current-proj-selector').click(function () {
    const projectId = $(this).data('project-id');
    if (projectId) {
        $.get(CONTEXT_PATH + '/user/setcurrentproject/' + projectId, function () {
            location.reload();
        });
    }
    else
        window.location.href = CONTEXT_PATH + '/project/list';
});
