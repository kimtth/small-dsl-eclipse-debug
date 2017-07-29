var g : num
def function(num a, num b, string c){

	val buff : string
	g = a[0]
	buff = numtostr(g)
	printstr(buff)
	
	return a
}

launch_main(){
	val a[1] : num
	val b[2][2] : num
	val c[3] : string

	a[0] = 100
	b[1][0] = 110
	b[0][1] = 010
	c[0] = "st"
	c[1] = "ri"
	c[2] = "ng"
	
	a = function(a, b, c)
	
	val buff : string
	g = a[0]
	buff = numtostr(g)
	printstr(buff)
}

