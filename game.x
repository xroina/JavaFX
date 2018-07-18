
locate 23,15
print "Press the button to start the game"
while button == 0 { }
while button != 0 { }
cls

x = 38
y = 28

score = 0
left = 3

while 1 {
	locate 0,0
	print "Score:" + score + " Left:" + left

	locate x, y
	print "Ａ\d\l\l\lI□I"

	if button {
		score = score + :game:fire(x, y)
	}

	ex = abs(rnd) % 76
	locate ex, 1
	print "(∀)"

	locate x, y
	print "  \d\l\l\l    "

	scrollPrev 1, 29

	if :game:dead(x, y) {

		left = left - 1

		if left < 1 {
			break
		}
		continue
	}

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
	if y < 2 {
		y = 1
	}

}
locate 0,0
print "Score:" + score + " Left:" + left

locate 34, 15
print " game over!! "
