	EQU	
	EQU	4
EN2	EQU	80000
	ORG	0
EN3	EQU	3
	DB
	DC.B	$M
	FCB	80000
	DW
RMB	80000
	DW   2  ;2
	DB   2
	DC.W 2
	DC.B 2
e1	FCB  2
	FDB  2222
	DS   34
	DS.B 34
	DS.W 34  ;68
e2	RMB  34  
	RMW  34 ;68
	FCC  "HOLA MUNDO"
	DC.W	@M
 DC.W	@8500
	FDB	80000
	rmw	@500  ;640
e3	swi
	FCC
	FCC	"
	FCC	"H
	FCC	H"
	DS
	FCB	$ff
	DS.B	$M
	RMB	80000
	org $0
e4	adca	$3
	DS.W
	DS.W	%M
	RMW	80000
EN5	EQU	2
e3	ldaa	5
fin	FCC "Solía \\ ñ \ ; mas"
EN4	END
