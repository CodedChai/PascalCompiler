   1. program TestProg;
   2. begin
   3. function newMult(x : integer; y : integer) : integer;
   4. var counter : integer;
   5. var returnme : integer;
   6. begin
   7. 	for counter := 1 to y do
   8. 	begin
   9. 		returnme = returnme + x;
  10. 	end;
  11. 	newMult := returnme;
  12. end;
  13. 
  14. a:= 100;
  15. b:= 0;
  16. c:= 10.20;
  17. d:= 10.455E3;
  18. e:= newMult(a,b);
  19. f:= a * b;
  20. if(a <> b) then
  21. 	c = 11.25;
  22. if(a > b) or (c <= d) then
  23. 	d = 5522.123E-25;
  24. 
  25. var myArray : array[1..10] of integer;
  26. end;EOF