	.text
	.file	"hello.ll"
	.globl	simp
	.align	16, 0x90
	.type	simp,@function
simp:                                   # @simp
	.cfi_startproc
# BB#0:
	pushq	%rbp
.Ltmp0:
	.cfi_def_cfa_offset 16
.Ltmp1:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp2:
	.cfi_def_cfa_register %rbp
	popq	%rbp
	retq
.Lfunc_end0:
	.size	simp, .Lfunc_end0-simp
	.cfi_endproc

	.globl	simple
	.align	16, 0x90
	.type	simple,@function
simple:                                 # @simple
	.cfi_startproc
# BB#0:
	pushq	%rbp
.Ltmp3:
	.cfi_def_cfa_offset 16
.Ltmp4:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp5:
	.cfi_def_cfa_register %rbp
	movl	%edi, -4(%rbp)
	popq	%rbp
	retq
.Lfunc_end1:
	.size	simple, .Lfunc_end1-simple
	.cfi_endproc

	.globl	simpleCall
	.align	16, 0x90
	.type	simpleCall,@function
simpleCall:                             # @simpleCall
	.cfi_startproc
# BB#0:
	pushq	%rbp
.Ltmp6:
	.cfi_def_cfa_offset 16
.Ltmp7:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp8:
	.cfi_def_cfa_register %rbp
	movl	%edi, -4(%rbp)
	popq	%rbp
	retq
.Lfunc_end2:
	.size	simpleCall, .Lfunc_end2-simpleCall
	.cfi_endproc

	.globl	notsimp
	.align	16, 0x90
	.type	notsimp,@function
notsimp:                                # @notsimp
	.cfi_startproc
# BB#0:
	pushq	%rbp
.Ltmp9:
	.cfi_def_cfa_offset 16
.Ltmp10:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp11:
	.cfi_def_cfa_register %rbp
	movq	%rdi, -8(%rbp)
	popq	%rbp
	retq
.Lfunc_end3:
	.size	notsimp, .Lfunc_end3-notsimp
	.cfi_endproc

	.globl	main
	.align	16, 0x90
	.type	main,@function
main:                                   # @main
	.cfi_startproc
# BB#0:
	pushq	%rbp
.Ltmp12:
	.cfi_def_cfa_offset 16
.Ltmp13:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
.Ltmp14:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	callq	simp
	movl	$2, %edi
	callq	simple
	movl	$0, -4(%rbp)
	xorl	%edi, %edi
	callq	simpleCall
	leaq	-4(%rbp), %rdi
	callq	notsimp
	xorl	%eax, %eax
	addq	$16, %rsp
	popq	%rbp
	retq
.Lfunc_end4:
	.size	main, .Lfunc_end4-main
	.cfi_endproc


	.ident	"clang version 3.8.0-2ubuntu4 (tags/RELEASE_380/final)"
	.section	".note.GNU-stack","",@progbits
