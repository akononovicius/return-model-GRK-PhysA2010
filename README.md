# Return model

This java program implements a model of absolute return in financial
markets based on a paper by V. Gontis, J. Ruseckas, A. Kononovicius
"Long-range memory stochastic model of the return in financial
markets" (arXiv:[0901.0903](https://arxiv.org/abs/0901.0903),
doi: [10.1016/j.physa.2009.09.011](https://dx.doi.org/10.1016/j.physa.2009.09.011)).

The model was implemented in 2009-2010 by Aleksejus Kononovicius,
other implementations were also used to produce results in the
aforemetioned paper.

Another implementation of the model (as well as model description)
is available at
[Physics of Risk](http://rf.mokslasplius.lt/long-range-memory-stochastic-model-return/)
blog and also on GitHub (see
[here](https://github.com/akononovicius/physRisk-model-repository/tree/main/grk-return-model)).

## --help output

```
The available options include:
  * Direct model parameters:
    --lambda or -l  This option sets lambda value used in model equations
                    (option followed by 1 double). Might be interpreted as
                    power of SDE stationary distribution power law tail.
    --epsilon or -e This options sets epsilon value used in model equations
                    (option followed by 1 double). Might be used to control
                    break point in PSD.
    --eta or -n     This options sets eta value used in model equations
                    (option followed by 1 double). Has a meanings of
                    stochastic multiplicativity.
    --kappa or -k   This options sets kappa value used in model equations
                    (option followed by 1 double). Model precission parameter.
    --tau or -t     This options sets tau value used by model (option
                    followed by 1 double). Dimensionless time window width
                    within which we integrated SDE sollutions.
    --tau60 or      This options sets tau60 value used by model (option
    -t60            followed by 1 double). Dimensionless time corresponding
                    to real time 60 seconds.
    --deltaT or -dT This options sets deltaT value used by model
                    (option followed by 1 double). Constant timestep size.
                    Numerical SDE solutions might become unstable with wrong
                    parameter value.
    --xmax or -x    This option sets differing exponential diffusion limit in
                    case of default diffusion limiter or (and) physical limit
                    to maximum return value in case if useLimits option
                    supplied (option followed by 1 integer).
    --oldLimiter    This options tells program to use old diffusion limiter
    or -ol          ((x*epsilon^n)^2).
    --useLimits     This option tells program to use physical limit while
    or -ul          modelling.
    --simpleSDE     This option tells program to use simple SDE while
    or -ss          modelling.
  * Noise parameters:
    --lambda2       This parameter sets lambda value used by noise formula
    or -l2          (option followed by 1 double). Basicly power of noise
                    distribution power law tail.
    --r0b or -rb    This parameter sets constant without MA(r) in r0() formula
                    near (option followed by 1 double). Constant noise
                    "variance".
    --r0a or -ra    This parameters set constant near MA(r) in r0() formula
                    (option followed by 1 double). Variable part of noise
                    "variance".
    --noNoise       This option disables usage of additional noise.
    or -nn
  * Mixed parameters (both model and software related):
    --experiment or This option allows to set experiment id (option followed 
    -ex             by integer). Experiment id can be used in output template.
                    If not set, experiment id equals Unix time.
    --core or -c    This options sets number of threads used for concurent
                    calculation (option followed by 1 integer). For best
                    preformance should equal to number of processor cores
                    available on machine.
    --points or -p  This option sets amount of points in each realization
                    (option followed by 1 integer).
    --realizations  This option sets amount of realizations calculated
    or -r           (option followed by 1 integer).
    --outPoints     This option sets amount of values calculated then
    or -op          calculating probability density function and spectra
                    (option followed by 1 integer).
    --pdfMax or     This option sets maximum value outputed then
    -pmx            calculating probability density function (option followed
                    by 1 integer).
    --pdfMin or     This option sets minimum value outputed then
    -pmn            calculating probability density function (option followed
                    by 1 integer).
    --notMinusMean  Options tells software not to subtract mean before
    or -!mm         calculating spectral density (might improve overlaping
                    with empirical results in term of magnitude).
    --approx or -a  This option sets approximation bounds which are used
                    then pdf and spectra aproximations are calculated
                    (option followed by 8 doubles or 1 string). PDF - first
                    two doubles; PSD, not simple SDE - 3rd to 6th double;
                    PSD, simple SDE - 7th and 8th double; String - "no".
    --approxDrop    This option forces output of approximated functions
    or -ad          to file. At least seven numbers are outputed to file
                    during this operation (pdf a, b coeficients, spectra
                    a1, b1, a2, b2 coefiencients and "break-point" in
                    spectra). Additionaly included numbers are from output
                    template. File name is set according to output template.
  * Directly related to software parameters:
    --version or -v This option forces output of version data asociated
                    with this program. No modelling is done.
    --license or -l This option forces output of license data asociated
                    with this program. No modelling is done.
    --window or -w  This option sets window parameters (option followed
                    by 2 integers and 1 string). Integers - width and height;
                    string can be equal to "centered" (or "c") or
                    "notCentered" (or "nc"). Doesn't imply graphical mode
                    (which is used by default).
    --image or -i   This option sets image parameters (option followed
                    by 2 integers and 1 string) and forces output to graphical
                    file. Integers - width and height; string - "png" or
                    "svg". Implies --notGui option.
    --notGui or -ng This option disables all windows but calculation progress
    or --!gui       window. Calculation results are outputed to text files.
    or -!g
    --commandLine   This option disables all windows. Calculation messages
    or -cl          are shown in terminal. Implies --notGui option.
    --noOut or -no  This option disables all output except for maintance
                    output (ex. error messages) and result output to text
                    file. Implies --commandLine option.
    --outTxt or -ot This option tells program to output results as text
                    files. Implies --notGui option.
    --output or -o  This option sets template of output directory and file
                    (option followed by 1 string). Some wild cards can be
                    used:
                      * "[l]" is substituted with lambda value,
                      * "[e]" is substituted with epsilon value,
                      * "[n]" is substituted with eta value,
                      * "[k]" is substituted with kappa value,
                      * "[t]" is substituted with tau value,
                      * "[l2]" is substituted with lambda2 value,
                      * "[rb]" is substituted with r0b value,
                      * "[ra]" is substituted with r0a value,
                      * "[ex]" is substituted with experiment id,
                      * "^" is substituted with empty string.
```
