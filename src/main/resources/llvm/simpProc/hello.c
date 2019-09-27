void simp()
{
	return;
}

void simple(int k)
{
	return;
}

void simpleCall(int k)
{

}

void notsimp(int* k)
{
	return;
}

int main(void)
{
	simp();
	simple(2);
	int k=0;
	simpleCall(k);
	notsimp(&k);
	notsimp(7);
}
