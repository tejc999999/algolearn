function learnCreateEditor() {
    var editor = ace.edit("editor");
	editor.setTheme("ace/theme/eclipse");
	editor.session.setMode("ace/mode/java");
	document.getElementById('editor').style.fontSize='12px';
	
	var editor_output = ace.edit("editor_output");
	editor_output.setTheme("ace/theme/twilight");
	editor_output.session.setMode("ace/mode/html");
	editor_output.setAutoScrollEditorIntoView(true);
	editor_output.setOption("minLines", 10);
	editor_output.setHighlightActiveLine(false);
	editor_output.setReadOnly(true); 
	editor_output.renderer.setShowGutter(false);
	
	var themeArray = [
		"chaos",
		"chrome",
		"clouds",
		"clouds_midnight",
		"cobalt",
		"crimson_editor",
		"down",
		"dracula",
		"dreamweaver",
		"eclipse",
		"github",
		"gob",
		"gruvbox",
		"idle_finger",
		"iplastic",
		"katzenmilch",
		"kr_theme",
		"kuroir",
		"merbivore",
		"merbivore_soft",
		"mono_industrial",
		"monokai",
		"pestel_on_dark",
		"solarized_dark",
		"solarized_light",
		"sqlserver",
		"terminal",
		"textmeta",
		"tomorrow",
		"tomorrow_night",
		"tomorrow_night_blue",
		"tomorrow_night_bright",
		"tomorrow_night_eighties",
		"twilight",
		"vibrant_ink",
		"xcode"
		];
	for(var i=0;i<themeArray.length;i++){
	  let op = document.createElement("option");
	  op.value = themeArray[i];  //value値
	  op.text = themeArray[i];   //テキスト値
	  document.getElementById("sel1").appendChild(op);
	}
}
function changeFontSize($this) {
    document.getElementById('editor').style.fontSize= $this.value + 'px';
}
function changeTheme() {
    editor.setTheme("ace/theme/" + document.getElementById("sel1").value);
}
function setCode() {
	var editor = ace.edit("editor");
	document.getElementById("code").value = editor.getValue();
    var form = document.getElementById('learnForm'); 
    form.submit();
}