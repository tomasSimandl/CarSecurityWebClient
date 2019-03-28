var positions;
var chartLabelsArr = [];
var chartSpeedArr = [];
var chartAltitudeArr = [];

/**
 * Method initialize data for charts and show chart for speed and for altitude.
 * @param pos list of routes positions.
 */
function setPositions(pos) {
    positions = pos;

    for (var i = 0; i < positions.length; i++) {
        chartLabelsArr.push(new Date(positions[i].time * 1000));
        chartAltitudeArr.push(positions[i].altitude);
        chartSpeedArr.push(positions[i].speed * 3.6);
    }

    getChart('speedChart', chartLabelsArr, chartSpeedArr, 'speed [km/h]');
    getChart('altitudeChart', chartLabelsArr, chartAltitudeArr, 'altitude [m]');
}

/**
 * Function load and display interactive Bing map on page in #mapPanel.
 */
function getMap() {
    var map = new Microsoft.Maps.Map(document.getElementById('mapPanel'), {
        /* No need to set credentials if already passed in URL */
        center: new Microsoft.Maps.Location(positions[0].latitude, positions[0].longitude),
        zoom: 16,
        mapTypeId: Microsoft.Maps.MapTypeId.road
    });
    var array = [];
    var i;
    for (i = 0; i < positions.length; i++) {
        array.push(new Microsoft.Maps.Location(positions[i].latitude, positions[i].longitude));
    }


    var polyline = new Microsoft.Maps.Polyline(positions, {strokeColor: 'Blue', strokeThickness: 2});
    map.entities.push(polyline);


    var pinStart = new Microsoft.Maps.Pushpin(array[0], {
        title: 'Start',
        color: 'green',
        text: 'S'
    });
    var pinEnd = new Microsoft.Maps.Pushpin(array[array.length - 1], {
        title: 'End',
        color: 'red',
        text: 'E'
    });
    map.entities.push(pinStart);
    map.entities.push(pinEnd);


    Microsoft.Maps.loadModule('Microsoft.Maps.WellKnownText', function () {
        document.getElementById('printoutPanel').innerHTML = Microsoft.Maps.WellKnownText.write(polyline);
    });
}

/**
 * Method create chart according to given parameters.
 * @param elementId is element in which will be chart created.
 * @param labels of x axes
 * @param data which should be displayed
 * @param yLabel label on y axes.
 */
function getChart(elementId, labels, data, yLabel) {
    var ctx = document.getElementById(elementId).getContext('2d');

    new Chart(ctx, {
        // The type of chart we want to create
        type: 'line',

        // The data for our dataset
        data: {
            labels: labels,
            datasets: [{
                backgroundColor: 'rgba(27, 88, 227, 0.3)',
                borderColor: 'rgb(0, 25, 112)',
                data: data
            }]
        },

        options: {
            elements: {line: {tension: 0}}, // disables bezier curves
            legend: {display: false},
            scales: {
                xAxes: [{
                    type: 'time',
                    time: {
                        displayFormats: {second: 'HH:mm:ss'}
                    }
                }],
                yAxes: [{
                    scaleLabel: {
                        labelString: yLabel,
                        display: true
                    }
                }]
            }
        }
    });
}