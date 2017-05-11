   1. program test (input, output);
   2.   var a : integer;
   3.   var b : real;
   4.   var c : array [1..2] of integer;
   5.   var d : real;
   6. 
   7.   procedure proc1(x:integer; y:real; 
   8.                   z:array [1..2] of integer; q: real);
   9.     var d: integer;
  10.     begin
  11.       a:= 2;
  12.       z[a] := 4;
  13.       c[3] := 3
  14.      end;
  15. 
  16.    procedure proc2(x: integer; y: integer);
  17.      var e: real;
  18.      var f: integer;
  19. 
  20.      procedure proc3(n: integer; z: real);
  21.        var e: integer;
  22. 
  23.        procedure proc4(a: integer; z: array [1..3] of real);
  24.          var x: integer;
  25.          begin
  26.            a:= e 
  27.          end;
  28. 
  29.        begin
  30.          a:= e;
  31.          e:= c[e]
  32.        end;
  33. 
  34.      begin
  35.        call proc1(x, e, c, b);
  36.        call proc3(c[1], e);
  37.        e := e + 4;
SEMERR: Incompatible types in addop operation.
  38.        a:= (a mod 4.4) div 4.4;
SEMERR: Incompatible types in mod operation.
  39.        while ((a >= 4.4) and ((b <= e)
SEMERR: Non bool type used for relop operation.
  40.                        or (not (a = c[a])))) do
  41.          begin
  42.            a:= c + 1.0 + #
SEMERR: Incompatible types in addop operation.
SYNERR: Expected ID, NUM, L_PAREN, NOT received: ADDOP
LEXERR: # commits error: Unrecog Symbol
  43.          end
  44.      end;
  45. 
  46. begin
  47.   call proc2(c[4],2);
SYNERR: Expected one of SEMI_C END received R_PAREN
  48.   if (a < 2) then a:= 1 else a := a + 2;
  49.   if (b > 4.2) then a := c[a]
  50. end.EOF
SYNERR: Expected one of SEMI_C END received DOT
SYNERR: Expected END received EOF
