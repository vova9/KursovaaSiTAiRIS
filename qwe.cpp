#include <stdio.h>
int main()
{
int n=100000,sum1=0,sum2=0,i,i2,k=0,g;
for (n=100000;n<1000000;n++)
  {   
	  
	  g=n;
	  for(i=0;i<3;i++)
	  {
      sum1+ =n%10;
	  n=n/10;
	  printf("%d", sum1/n);
	  }
	  
	  for(i2=0;i2<3;i2++)
	  {
      sum2+ =n%10;
	  n=n/10;

	  }

	  if (sum1==sum2) 
	  {
		  printf("%d",g);
       k++;
	  }


  } 

printf("%d",k);

return 0;
}