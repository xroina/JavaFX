
x = @0
y = @1

left = 0

ch1 = character(x, y)
ch2 = character(x+1,y)
ch3 = character(x+2,y+1)
ch4 = character(x-1,y+1)

if(ch1 != " " && ch1 != "" || ch2 != " " && ch2 != ""
|| ch3 != " " && ch3 != "" || ch4 != " " && ch4 != "") {
	while button == 0 {
		locate x,y
		print "XX\d\l\l\lXXXX"

		locate x, y
		print "  \d\l\l\l    "

		locate 34, 15
		print " you died!! "

	}

	left = 1
	cls

}

return left