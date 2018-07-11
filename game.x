x=38
y=28

while(1) {
	locate x, y
	print "Ａ"
	locate x-1, y+1
	print "I□I"

	locate x, y
	print "  "
	locate x-1, y+1
	print "    "

	dx = stickX
	dy = stickY
	x = x + int(abs(dx) + 0.5) * sig(dx)
	y = y + int(abs(dy) + 0.5) * sig(dy)

	if x > 76 {
		x = 76
	}
	if x < 1 {
		x = 1
	}

	if y > 28 {
		y = 28
	}
	if y < 1 {
		y = 1
	}

}
