def function(num a, string b, num c, string d){
	val b : string
	b = "string"

	innerfunction(a, "sssss")
	printstr("function")
	
	return a
}

def innerfunction(num a, string b){

	printstr("before innerfunction2")
	a = innerfunction2(a, b)
	
	b = numtostr(a)
	printstr(b)
	printstr("innerfunction")
}

def innerfunction2(num a, string c){
	a = 220
	printstr("innerfunction2")

	return a
}

launch_main(){
	val a : num
	val b : string
	
	a = 100
	a = function(a,b,a,b)
}