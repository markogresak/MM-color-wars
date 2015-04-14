var WEBSOCKET_ADDRESS = 'ws://localhost:8887';

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
    saveChartToImg();
    saveWon();
    colors = data.colors;
    probabilities = data.p;
    displayProbabilites(probabilities, colors);
    initChart(colors);
    ws.send('init-ok');
    initDone = true;
  } else if (data.message && data.message === 'chart') {
    // drawWonChart(data.value);
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
      type: "line",
      dataPoints: datapoints[i],
      markerType: "none"
    });
  }
  initDatapoints();

  CanvasJS.addColorSet("graphColors", colors);

  chart = new CanvasJS.Chart("chartContainer", {
    title: {
      text: "Delež barv"
    },
    colorSet: "graphColors",
    height: 500,
    axisX: {
      title: "čas [iteracije]",
      minimum: 0
    },
    axisY: {
      title: "verjetnost",
      minimum: 0,
      maximum: 1.0
    },
    data: chartData
  });

  return chart;
}

function saveChartToImg() {
  try {
    if (datapoints) {
      var canvas = document.querySelector("#chartContainer canvas");
      var img = canvas.toDataURL("image/png");
      var imageContainer = document.getElementById("slike");
      var newImage = new Image();
      newImage.src = img;
      imageContainer.appendChild(newImage);
    }
  } catch (e) {}
}

function updateChart(data) {
  var d = data[0],
    lastD = datapoints[0][datapoints[0].length - 1];
  if (d && lastD && d.x < lastD.x) {

    initDatapoints();
  }
  data.map(function(e, i) {
    datapoints[i].push(e);
  });
  chart.render();
}

$(function() {
  chart = null;
  initDone = false;
  connectWS(messageRecieved);
});
