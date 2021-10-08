##OpCodes description:

####OP_ICONST - load constant value
The purpose of this instruction is load constant value into register. 
By constant value is meant the value that can fit in 2 bytes and can be placed in 
instruction in bytecode file.
According to format, ICONST instructure has this structure:
```
Instruction {
    opcode = OP_ICONST,
    src_1 = <high byte>
    src_2 = <low byte>
    dst = <register num>
}
```