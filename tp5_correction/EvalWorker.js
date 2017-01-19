onmessage = function(e) {
    var notes = JSON.parse(e.data);
    var coords = {};
    var max = notes[0];
 for(var i =1;i<=4;i++) {
      var note=notes[i];
      coords.x=i-1;
        for (var j=0;j<max;j++) {
               coords.y = (note*j/max)*50;
                    //exemple pas super car l'affichage du graphique est qd même trop long !!             
                    //postMessage(JSON.stringify(coords));

            }
          //affiche en une fois mais bcp plus rapide que sans worker   
          postMessage(JSON.stringify(coords));
        }

}