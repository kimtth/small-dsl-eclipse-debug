var a : num
var ax : num
var b : string
var ca[2] : num
var da[2][2] : string
var e : bool
var gl : num

def function(num a, string b, num c, string d){
	a = 10
	b = "string"

	b = numtostr(gl)
	b = strjoin("abcd", b)
	printstr(b)

	val local : num
	val localStr : string

	if(a == ax){
		printstr("if")
	}else{
		printstr("else")
	}

	ca[0] = 100
	ca[1] = 200

	da[0][1] = "A100"
	da[1][1] = "B200"

	local = 100
	localStr = "localStr"

	a = 0
	while(a < 3){
		printstr("while")
		a = a + 2
	}
	
	b = numtostr(a)
	printstr(b)
	while(a < 10){
		a = a + 5
		b = numtostr(a)
		printstr(b)
		if(a != ax){
			break
		}
		printstr("break")
	}

	innerfunction(local, localStr)
	printstr("function")
	return a
}

def innerfunction(num local, string localStr){

	if(localStr != numtostr(local)){
		printstr("equals")
	}

	local = innerfunction2(local, localStr)
	
	b = numtostr(local)
	printstr(b)
	printstr("innerfunction")
}

def innerfunction2(num local, string localStr){
	local = 220
	printstr("innerfunction2")

	return local
}

launch_main(){
	val a : num
	val ax : num
	val b : string
	val local : num

	gl = 100

	a = function(a,b,a,b)
}