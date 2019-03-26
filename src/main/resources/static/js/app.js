
function sendAjax(url, method, data, successFunction, failFunction){
    $.ajax({
        url: url,
        type: method,
        data: data
    })
        .done(successFunction)
        .fail(failFunction)
    ;
}

function showFail(data){
    $('#toast-body').text(data.responseJSON.error);
    $('.toast').toast('show');
}


// ======================================================= EVENT =======================================================
function deleteEvent() {
    var url = '/event';
    var eventId = $('#btn-delete').data('event-id');
    var data = {event_id: eventId };
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'DELETE', data, successFunc, showFail);
}

function submitFormEvent() {
    var url = '/event';
    var formData = $('#event-modal-form').serializeArray();
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'PUT', formData, successFunc, showFail)
}

function initEventModal() {
    $('#eventModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var eventId = button.data('event-id');

        var modal = $(this);
        modal.find('#event-note').val(button.data('note'));
        modal.find('#event-id').val(eventId);
        modal.find('#btn-delete').data('event-id', eventId);
    });
}

function loadNextPage(url) {
    var button = $('#btn-load-next');
    var page = button.data('page') + 1;
    // var url = '/event?page=' + page;

    $.get(url + page)
        .done(function (data) {
            if(data === ''){
                button.hide()
            } else {
                $('#cards-container').append(data);
                button.data('page', page)
            }
        })
        .fail(function (data) {
            $('#toast-body').text(data.responseJSON.error);
            $('.toast').toast('show');
        })
}
// ======================================================== CAR ========================================================
function deleteCar() {
    var url = '/car';
    var carId = $('#btn-delete').data('car-id');
    var data = {car_id: carId};
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'DELETE', data, successFunc, showFail);
}

function submitFormCar() {
    var url = '/car';
    var formData = $('#car-modal-form').serializeArray();
    var method = ($('#car-id').val() === '') ? 'POST' : 'PUT';
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, method, formData, successFunc, showFail)
}

function initCarModal() {
    $('#carModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);

        var modal = $(this);
        modal.find('#car-note').val(button.data('note'));
        modal.find('#car-name').val(button.data('name'));
        modal.find('#car-modal-label').text(button.data('title'));

        if (button.data('show-delete') === false) {
            modal.find('#btn-delete').hide();
            $('#car-id').val("");
        } else {
            var btnDelete = modal.find('#btn-delete');
            btnDelete.show();
            btnDelete.data('car-id', button.data('car-id'));
            $('#car-id').val(button.data('car-id'));
        }
    });
}

// ======================================================= ROUTE =======================================================

function deleteRoute(routeId) {
    var url = '/route';
    var data = {route_id: routeId };
    var successFunction = function() {
        window.location.href = "/route"
    };

    sendAjax(url, 'DELETE', data, successFunction, showFail);
}

// ====================================================== STATUS =======================================================

function refreshStatus(carId) {

    var spinner = $("#spinner-" + carId);
    spinner.show();
    var reloadBtn = $("#btn-status-" + carId);
    reloadBtn.prop('disabled', true);

    var url = '/status';
    var data = {car_id: carId};
    var method = 'GET';
    var successFunc = function (status) {
        spinner.hide();
        reloadBtn.prop('disabled', false);
        $('#table-car-' + carId).replaceWith(status)
    };
    var failFunc = function (data) {
        spinner.hide();
        reloadBtn.prop('disabled', false);
        showFail(data)
    };

    sendAjax(url, method, data, successFunc, failFunc)
}

function activateTool(carId, tool) {
    var url = '/tool/activate';
    switchTool(carId, tool, url)
}

function deactivateTool(carId, tool) {
    var url = '/tool/deactivate';
    switchTool(carId, tool, url)
}

function switchTool(carId, tool, url) {
    var data = {car_id: carId, tool: tool};
    var method = 'POST';
    var successFunc = function () { refreshStatus(carId) };

    sendAjax(url, method, data, successFunc, showFail)
}

// ======================================================= USER ========================================================

function updateEmail() {

    var email = $("#input_email").val();
    var userId = $("#input_id").val();

    var url = '/user';
    var data = {id: userId, email: email};
    var method = 'PUT';
    var successFunc = function () { window.location.href = '/settings'; };

    sendAjax(url, method, data, successFunc, showFail)
}

function deleteUser(userId) {
    var url = '/user';
    var data = {id: userId};
    var successFunc = function () {
        sendAjax('/logout', 'POST', null, null, null);
        window.location.href = '/login';
    };

    sendAjax(url, 'DELETE', data, successFunc, showFail);
}