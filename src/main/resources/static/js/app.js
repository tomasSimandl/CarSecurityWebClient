/**
 * Function send ajax specified by input parameters.
 * @param url endpoint where will be send request.
 * @param method method which should be used POST, PUT, DELETE,...
 * @param data serialized data which should be sent.
 * @param successFunction function which is called on success
 * @param failFunction function which is called on fail
 */
function sendAjax(url, method, data, successFunction, failFunction) {
    $.ajax({
        url: url,
        type: method,
        data: data
    })
        .done(successFunction)
        .fail(failFunction)
    ;
}

/**
 * Method show error message to user.
 * @param data json which contains attribute error with error message.
 */
function showFail(data) {
    $('#toast-body').text(data.responseJSON.error);
    $('.toast').toast('show');
}


// ======================================================= EVENT =======================================================
/**
 * Send request to delete event which is specified with #btn-delete data.
 */
function deleteEvent() {
    var url = '/event';
    var eventId = $('#btn-delete').data('event-id');
    var data = {event_id: eventId};
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'DELETE', data, successFunc, showFail);
}

/**
 * Send request to update event according to data in #event-modal-form
 */
function submitFormEvent() {
    var url = '/event';
    var formData = $('#event-modal-form').serializeArray();
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'PUT', formData, successFunc, showFail)
}

/**
 * Initialize modal for update events.
 */
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

/**
 * Function load and show next page of events.
 * @param url of endpoint witch ends with 'page=.
 */
function loadNextPage(url) {
    var button = $('#btn-load-next');
    var page = button.data('page') + 1;
    // var url = '/event?page=' + page;

    $.get(url + page)
        .done(function (data) {
            if (data === '') {
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
/**
 * Function send request to delete car which is specified by #btn-delete data.
 */
function deleteCar() {
    var url = '/car';
    var carId = $('#btn-delete').data('car-id');
    var data = {car_id: carId};
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'DELETE', data, successFunc, showFail);
}

/**
 * Method send create or update request to car endpoint. On fail error message is displayed. On success page is reload.
 */
function submitFormCar() {
    var url = '/car';
    var formData = $('#car-modal-form').serializeArray();
    var method = ($('#car-id').val() === '') ? 'POST' : 'PUT';
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, method, formData, successFunc, showFail)
}

/**
 * Function initialized modal for car update.
 */
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

/**
 * Method send request to delete route. On fail error message is displayed. On success is redirect to route page.
 * @param routeId identification of route which should be deleted.
 */
function deleteRoute(routeId) {
    var url = '/route';
    var data = {route_id: routeId};
    var successFunction = function () {
        window.location.href = "/route"
    };

    sendAjax(url, 'DELETE', data, successFunction, showFail);
}

// ====================================================== STATUS =======================================================

/**
 * Method send refresh request of given car. On fail error message is displayed. On success load data are displayed.
 * @param carId specification of car which status should be load.
 */
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

/**
 * Method send request to activate given tool.
 * On fail toast with error is displayed.
 *
 * @param carId identification of car of which tool should be activated.
 * @param tool which status will be activated.
 */
function activateTool(carId, tool) {
    var url = '/tool/activate';
    switchTool(carId, tool, url)
}

/**
 * Method send request to deactivate given tool.
 * On fail toast with error is displayed.
 *
 * @param carId identification of car of which tool should be deactivated.
 * @param tool which status will be deactivated.
 */
function deactivateTool(carId, tool) {
    var url = '/tool/deactivate';
    switchTool(carId, tool, url)
}

/**
 * Method send request to switch status of given tool.
 * On fail toast with error is displayed.
 * @param carId identification of car of which tool should be switched.
 * @param tool which status will be switched.
 * @param url url address of endpoint.
 */
function switchTool(carId, tool, url) {
    var data = {car_id: carId, tool: tool};
    var method = 'POST';
    var successFunc = function () {
        refreshStatus(carId)
    };

    sendAjax(url, method, data, successFunc, showFail)
}

// ======================================================= USER ========================================================

/**
 * Method send request to update email. email address is taken from #input_email and users id is from #input_id.
 * On fail toast with error is displayed.
 */
function updateEmail() {

    var email = $("#input_email").val();
    var userId = $("#input_id").val();

    var url = '/user';
    var data = {id: userId, email: email};
    var method = 'PUT';
    var successFunc = function () {
        window.location.href = '/settings';
    };

    sendAjax(url, method, data, successFunc, showFail)
}

/**
 * Function sends request to delete input user on success logout request is send. On fail toast with error is
 * displayed.
 * @param userId identification of user which should be deleted.
 */
function deleteUser(userId) {
    var url = '/user';
    var data = {id: userId};
    var successFunc = function () {
        sendAjax('/logout', 'POST', null, null, null);
        window.location.href = '/login';
    };

    sendAjax(url, 'DELETE', data, successFunc, showFail);
}