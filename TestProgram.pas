program default(input, output);
var num1 : integer; var num2: integer;
var a : integer;
var b : real;
var c : array [1..2] of integer;
procedure square(x : integer; num : integer);
begin
    c[1] := 4
end;

procedure slowMult(x : integer; y: integer);
var temp:integer; var num: integer;
begin
    temp := 0;
    num := 0;
    while temp < y do
    begin
        num := num + x ;
        temp := temp + 1
    end;
    call writeln(num)
end;

begin
    call slowMult(1, 5);
    call slowMult(2, 7)
end.