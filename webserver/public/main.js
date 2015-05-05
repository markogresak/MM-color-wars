var WEBSOCKET_ADDRESS = 'ws://localhost:8887';
var HISTOGRAM_T = 10;

/**
 * Attempt to establish new websockets connection
 * @param {Function} messageCB onmessage function callback.
 */
function connectWS(messageCB) {
  // Init websockets with globally defined address.
  ws = new WebSocket(WEBSOCKET_ADDRESS);
  ws.onopen = function() {
    // Print a message when connection opens.
    console.log('open on ' + WEBSOCKET_ADDRESS);
  };
  ws.onclose = function() {
    initDone = false;
    // Print a message when connection closes.
    console.log('Websocket closed. Trying to reconnect.');
    // Call this function again (try to reconnect).
    setTimeout(function() {
      connectWS(messageCB);
    }, 1000);
  };
  // Set passed function as onmessage callback.
  ws.onmessage = messageCB;
}

/**
 * Websocket onmessage callback function.
 * @param {Object} e Event (with `data`) passed from server.
 */
function messageRecieved(e) {
  var data = JSON.parse(e.data);
  if (data.message && data.message === 'init' && !initDone) {
    // saveChartToImg();
    saveWon();
    colors = data.colors;
    probabilities = data.p;
    displayProbabilites(probabilities, colors);
    initChart(colors);
    ws.send('init-ok');
    initDone = true;
  } else if (chart && !data.message) {
    updateChart(data);
  }
}

function initDatapoints() {
  if (datapoints) {
    for (var i = 0; i < datapoints.length; i++) {
      datapoints[i].splice(0);
    }
  }
}

function initChart(colors) {
  datapoints = [];
  var chartData = [];
  for (var i = 0; i < 10; i++) {
    datapoints.push([]);
    chartData.push({
      type: 'line',
      dataPoints: datapoints[i],
      markerType: 'none'
    });
  }
  initDatapoints();

  CanvasJS.addColorSet('graphColors', colors);

  chart = new CanvasJS.Chart('chartContainer', {
    title: {
      text: 'Color percentage'
    },
    colorSet: 'graphColors',
    height: 500,
    axisX: {
      title: 'time [iterations]',
      minimum: 0
    },
    axisY: {
      title: 'percentage',
      minimum: 0,
      maximum: 1.0
    },
    data: chartData
  });

  return chart;
}

function saveWon() {
  try {
    if (datapoints) {
      var i;
      for (i = 0; i < datapoints.length; i++) {
        var y = datapoints[i][datapoints[i].length - 1];
        if (y === 1) {
          break;
        }
      }
      wonIndices.push(i);
    }
  } catch (e) {}
}

function saveChartToImg() {
  console.log('try save');
  try {
    if (datapoints) {
      var canvas = document.querySelector('#chartContainer canvas');
      var img = canvas.toDataURL('image/png');
      var imageContainer = document.getElementById('slike');
      var newImage = new Image();
      newImage.src = img;
      imageContainer.appendChild(newImage);
      console.log('saved');
    }
  } catch (e) {}
}

function updateChart(data) {
  var color = data[0],
    lastColor = datapoints[0][datapoints[0].length - 1];
  if (color && lastColor && color.x <= lastColor.x) {
    saveChartToImg();
    initDatapoints();
  }
  data.map(function(e, i) {
    datapoints[i].push(e);
  });
  chart.render();
}

function displayProbabilites(data, colors) {
  valuesEl.empty();
  for (var i = 0; i < data.length; i++) {
    var p = data[i];
    var c = colors[i];
    valuesEl.append($('<span>')
      .addClass('value')
      .text(p)
      .css('border', '8px solid ' + c));
  }
}

$(function() {
  chart = null;
  histogram = null;
  wonIndices = [];
  wonChartData = [];
  valuesEl = $('#values');
  initDone = false;
  connectWS(messageRecieved);
});
