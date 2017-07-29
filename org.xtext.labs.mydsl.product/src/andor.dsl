def function(num a, num b){

	val boola : bool
	val boolb : bool
	
	boola = true
	boolb = true
	
	if(boola and boolb){
		printstr("and")
	}
	
	boola = true
	boolb = false
	
	if(boola or boolb){
		printstr("or")
	}
	
	if(boola and boolb){
		printstr("do nothing")
	}else{
		printstr("not and")
	}
}

launch_main(){
	val a : num
	val b : num
	val c : string

	function(a, b)
}

