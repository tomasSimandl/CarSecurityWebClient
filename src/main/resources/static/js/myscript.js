
function sendAjax(url, method, data, successFunction){
    $.ajax({
        url: url,
        type: method,
        data: data
    })
        .done(successFunction)
        .fail(function (data) {
            $('#toast-body').text(data.responseJSON.error);
            $('.toast').toast('show');
        })
    ;
}


// ======================================================= EVENT =======================================================
function deleteEvent() {
    var url = '/event';
    var eventId = $('#btn-delete').data('event-id');
    var data = {event_id: eventId };
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'DELETE', data, successFunc);
}

function submitFormEvent() {
    var url = '/event';
    var formData = $('#event-modal-form').serializeArray();
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, 'PUT', formData, successFunc)
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

    sendAjax(url, 'DELETE', data, successFunc);
}

function submitFormCar() {
    var url = '/car';
    var formData = $('#car-modal-form').serializeArray();
    var method = ($('#car-id').val() === '') ? 'POST' : 'PUT';
    var successFunc = function () {
        location.reload();
    };

    sendAjax(url, method, formData, successFunc)
}

function initCarModal() {
    $('#carModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);

        var modal = $(this);
        modal.find('#car-icon').val(button.data('icon'));
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

    sendAjax(url, 'DELETE', data, successFunction);
}