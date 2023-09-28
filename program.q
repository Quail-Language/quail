#:alias "<-" return
#:alias "\\?" if
#:alias "\\|" else
#:alias "<<" print
#:alias ">>" input
#:alias "::" for


#<<(((n)-><-[((l,i)->? i<2 <- i | <- l[-2]+l[-1])(_this,i) :: i in 0:n])(num(>>())))

throw Exception() % { "message" = "This is an exception" }
