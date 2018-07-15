
x = @0
y = @1

score = 0

ch1 = ""
ch2 = ""

# debug "fire"

for ly = y-1 ly > 0 ly-- {
	ch1 = character(x, ly)
	ch2 = character(x+1, ly)

	if ch1 != " " && ch1 != "" || ch2 != " " && ch2 != "" {
		break
	}
	locate x, ly
	print "┃"
}

if ly > 0 {
	lx = x
	if ch1 == ")" || ch1 == "∀" {
		for lx = x-1 lx > 0 lx-- {
			if character(lx, ly) == "(" {
				break
			}
		}
	}
	if ch2 == "(" {
		lx = x + 1
	}

	if character(lx, ly) == "(" {
		for i=0 i<5 i++ {
			locate lx, ly
			print "XXXX"

			locate lx, ly
			print "    "
			score = 100
		}
	}
}

for(yy = y - 1; yy > ly; yy--) {
	locate x, yy
	print "  "
}

return score
