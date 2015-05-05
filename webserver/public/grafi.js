function loadData(callback, samples, n, i) {
  samples = samples || 10;
  n = n || 25;
  i = i || 10;
  $.getJSON(`/data/graph-${samples}-${n}x${n}-i${i}.json`, callback);
}

function makeGraphs(data) {
  var grafiData = [];
  for (var i = 0; i < data.length; i++) {
    grafiData.push({
      type: 'line',
      lineThickness: 1,
      dataPoints: data[i],
      markerType: 'none'
    });
  }

  CanvasJS.addColorSet('graphColors', ['#1FA2CE']);
  // CanvasJS.addColorSet('graphColors', ['#7C078E']);

  chart = new CanvasJS.Chart('grafiContainer', {
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
    data: grafiData
  });
  chart.render();
}

function updateView() {
  var samples = samplesEl.val();
  var n = sizenEl.val();
  var i = iEl.val();
  loadData(makeGraphs, samples, n, i);
}

function saveChartToImg() {
  try {
    var canvas = document.querySelector('#grafiContainer canvas');
    var img = canvas.toDataURL('image/png');
    var imageContainer = document.getElementById('slike');
    var newImage = new Image();
    newImage.src = img;
    imageContainer.appendChild(newImage);
  } catch (e) {}
}

$(function() {
  samplesEl = $('#samples');
  sizenEl = $('#sizen');
  iEl = $('#sizei');
  updateView();
  $('select').change(updateView);
});
