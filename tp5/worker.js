onmessage = function (e) {
	var note = JSON.parse(e.data);
	var nbLoop = 10000;
	var hauteur = 0;
	increment = note/nbLoop;
	for (var y = 0; y < nbLoop; y++) {
		hauteur += increment;
	}
	postMessage(JSON.stringify(hauteur));
}