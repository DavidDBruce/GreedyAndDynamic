package AlgorithmsProject2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class AlgorithmsProjectTwo {
	
	static ArrayList<Item> items = new ArrayList<>();

	public static void main(String[] args) 
	{
		Scanner s = new Scanner(System.in);
		Random ran = new Random();
		int numberOfItems = 0;
		int sizeOfKnapsack = 0;
		boolean pass = false;
		
		System.out.println("In one positive integer, how big is your knapsack?");
		while(!pass)
		{
			int tempInput = Integer.parseInt(s.nextLine());
			if(tempInput==0)
			{System.out.println("You can't carry anything. Go get a knapsack and tell me how big it is.");}
			else
			{sizeOfKnapsack = tempInput; pass=true;}
		}
		pass = false;
		System.out.println("Enter 0 for a random number of items up to 1000 or any (positive) integer for that number of items.");
		numberOfItems = Integer.parseInt(s.nextLine());
		while(!pass)
		{
			if(numberOfItems == 0)
			{
				numberOfItems = ran.nextInt(1000) + 1;
				pass = true;
			}
			else if(numberOfItems < 0)
			{
				System.out.println("We need a positive number, this isnt science fiction.");
			}
			else
			{
				pass = true;
			}
		}
		
		pass = false;
		System.out.println("Enter 0 for a list of heavy low value items.");
		System.out.println("Enter 1 for a list of light high value items.");
		System.out.println("Enter 2 for a list of balanced items.");
		System.out.println("Enter 3 for a list of randomly generated items.");
		while(!pass)
		{
			int tempInput = Integer.parseInt(s.nextLine());
			if(tempInput==0)
			{
				createLowValueHighWeight(numberOfItems);
				pass = true;
			}
			else if(tempInput==1)
			{
				createHighValueLowWeight(numberOfItems);
				pass = true;
			}
			else if(tempInput==2)
			{
				createBalancedItems(numberOfItems);
				pass = true;
			}
			else if(tempInput==3)
			{
				createRandomItems(numberOfItems);
				pass = true;
			}
			else
			{
				System.out.println("Please enter one of the specified numbers.");
			}
			
		}
		
		pass = false;
		System.out.println("Enter 0 for Greedy.");
		System.out.println("Enter 1 for Dynamic1");
		
		while(!pass)
		{
			int tempInput = Integer.parseInt(s.nextLine());
			if(tempInput==0)
			{
				double totalValue;
				long time = 0;
				Greedy greedy = new Greedy(items,sizeOfKnapsack);
				time = System.nanoTime();
				totalValue = greedy.runGreedy();
				time -= System.nanoTime();
				
				System.out.println("In " + (-1*time) + "ns, you made it out with " + totalValue + "v in your " + sizeOfKnapsack + "w knapsack. Items:" + items.size() + ".");
				pass = true;
			}
			if(tempInput==1)
			{
				Dynamic1 dynamic1 = new Dynamic1(items,sizeOfKnapsack);
				int totalValue;
				long time = 0;
				time = System.nanoTime();
				totalValue = dynamic1.runDynamic1();
				time -= System.nanoTime();
				
				System.out.println("In " + (-1*time) + "ns, you made it out with " + totalValue + "v in your " + sizeOfKnapsack + "w knapsack. Items:" + items.size() + ".");
				pass = true;
			}
			else
			{
				System.out.println("Please enter one of the specified numbers.");
			}
		}
		
		s.close();
	}
	
	static void createLowValueHighWeight(int size)
	{
		items = new ArrayList<>();	
		for(int i = 0; i < size; i++)
		{
			items.add(new Item(size*5, size));
		}
	}
	static void  createHighValueLowWeight(int size)
	{
		items = new ArrayList<>();
		for(int i = 0; i < size; i++)
		{
			items.add(new Item(size, size*5));
		}
	}
	static void createBalancedItems(int size)
	{
		items = new ArrayList<>();
		int j = size;
		for(int i = 0; i < size; i++)
		{
			items.add(new Item(i+1,j));
			j--;
		}
	}
	static void createRandomItems(int size)
	{
		items = new ArrayList<>();
		Random r = new Random();
		for(int i = 0; i < size; i++)
		{
			items.add(new Item(r.nextInt(99)+1,r.nextInt(99)+1));
		}
	}
	
}

class Item{
	
	int weight;
	double value;
	double VWRatio;
	
	Item(int inputWeight, int inputValue)
	{
		weight = inputWeight;
		value = inputValue;
		VWRatio = value/weight;
	}
}

class Greedy{
	ArrayList<Item> items = new ArrayList<>();
	int W = 0;
	Greedy(ArrayList<Item> items, int W){
		this.items = items;
		this.W = W;
		
	}
	
	public double runGreedy()
	{	
		sortItems();
		double totalValue = 0.00;
		//for(int i = items.size()-1; i>=0; i--)
		for(int i = 0; i<items.size()-1; i++)
		{
			if(items.get(i).weight <= W)
			{
				totalValue += items.get(i).value;
				W -= items.get(i).weight;
			}
			else return totalValue;
			
		}
		return totalValue;
	}
	
	public void sortItems()
	{
		ArrayList<Item> sortedItems = new ArrayList<>();
		sortedItems = items;
		for(int i = 0; i < items.size(); i++)
		{
			
			int smallestIndex = i;
			
			for(int j = i; j < items.size(); j++)
			{
				
				if(sortedItems.get(smallestIndex).VWRatio > sortedItems.get(i).VWRatio)
				{	
					smallestIndex = j;
				}
				if(j == items.size()-1)
				{
					
					Item swapHolder = sortedItems.get(smallestIndex);
					
					
					sortedItems.set(smallestIndex, sortedItems.get(i));
					
					
					sortedItems.set(i, swapHolder);
					
				}
				
			}
			
		}	
		items = sortedItems;
	}
}

class Dynamic1
{
	ArrayList<Item> items = new ArrayList<>();
	int W = 0;
	Dynamic1(ArrayList<Item> items, int W)
	{
		this.items = items;
		this.W = W;
	}
	
	public int runDynamic1()
	{
		int n = items.size();
		int[][] T = new int[n][W+1];
		for(int i = 0; i<n; i++)
		{
			T[i][0] = 0;
		}
		for(int j = 1; j<W+1; j++)
		{
			if(items.get(0).weight <= j)
			{
				T[0][j] = (int) items.get(0).value;
			}
			else
			{
				T[0][j] =0;
			}
		}
		for(int i = 1; i<n; i++)
		{
			for(int j = 1; j<W+1; j++)
			{
				if(items.get(i).weight < j)
				{
					T[i][j] = (int) Math.max(T[i-1][j], items.get(i).value + T[i-1][j-items.get(i).weight]);
				}
				else
				{
					T[i][j] = T[i-1][j];
				}
			}
		}
		
		return T[n-1][W];
	}
}