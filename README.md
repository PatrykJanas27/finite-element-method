# finite-element-method

## This project is developed for the course "Finite Element Methods" at the AGH University of Science and Technology.

## Description
Finite Element Method (FEM) is a numerical analysis technique used to solve engineering and mathematical 
problems by dividing a complex physical system into smaller, simpler parts called finite elements.
FEM involves dividing a continuous system into a finite number of discrete elements or sub-regions, 
called mesh, and then applying numerical methods to approximate the behavior of the system.

In FEM, the system is modeled by a set of mathematical equations and the solution is obtained by approximating 
these equations for each element in the mesh. The method involves using a set of basis functions to represent 
the solution within each element, and then integrating the basis functions over each element to obtain 
the solution for the entire system.

FEM is a powerful tool for solving complex problems. FEM has many applications in engineering, 
including structural analysis, fluid dynamics, heat transfer, and electromagnetic analysis. 
It is used in a wide range of industries, including automotive, aerospace, civil engineering, and manufacturing.

The aim of the laboratory exercises was to write a program to solve and simulate heat flow in a material divided into 
smaller finite elements. To conduct the simulation, we used Fourier's equation, where the temperature gradient is 
the driving force of heat exchange in the material. We only dealt with convection. 
To conduct such a simulation, it was necessary to calculate the temperature at each node, 
and by interpolation, a continuous distribution can be obtained. We read the finite element meshes and basic data 
related to the simulation, which determined, among other things, the minimum ambient temperature, 
the heat transfer coefficient, density, initial temperature, simulation time, etc. from .txt files provided by 
the laboratory exercise instructor. 

During the course, it was necessary to calculate the appropriate matrices, i.e. the [H] matrix, the [HBC] matrix, 
the [C] matrix, the {P} vector, and introduce time into the solutions to finally obtain specific results. 
In the program for calculating integrals, we used Gaussian quadrature, which minimized the numerical error.

We used the transformation Jacobians to switch between two different coordinate systems, i.e. local and global, 
which are a characteristic feature and advantage of finite element methods. 
The Jacobian is the derivative of the transformation between two different coordinate systems. 
It is a scaling factor that determines how the transformation changes the area or volume at a given point. 
To switch from the local system, in our case using the axes ksi and eta, to the global system, using the axes x and y,
we need to use the inverse Jacobian of the transformation.

# result of simulation (example for mixGrid mesh)
![](https://i.imgur.com/ZjFnKDx.jpg)

# conclusions
These simulations are mainly carried out in order to save time and costs - they allow for testing and analysis without 
the need to build a physical prototype. Conducting simulations, first and foremost, allowed me to understand 
the complex physical processes occurring in the material. The temperature results from the computer program we wrote
during the classes match the results provided by the instructor. The program was written for 2, 3, and 4 integration
points for a 9-element straight and mixGrid mesh.



